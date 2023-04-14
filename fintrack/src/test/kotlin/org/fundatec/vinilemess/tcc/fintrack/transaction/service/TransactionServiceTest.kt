package org.fundatec.vinilemess.tcc.fintrack.transaction.service

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.Transaction
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionExpense
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionIncome
import org.fundatec.vinilemess.tcc.fintrack.transaction.repository.TransactionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class TransactionServiceTest {

    private val transactionRepository: TransactionRepository = mockk()
    private val transactionService = TransactionService(transactionRepository)
    private val testDate = LocalDate.of(2023, 4, 13)
    private val testUserSignature = UUID.randomUUID().toString()
    private val testPositiveAmount = BigDecimal.TEN
    private val testNegativeAmount = BigDecimal.TEN.negate()
    private val testDescription = "test"

    @Test
    fun `Should return persisted transaction when transactIncome is called`() {
        val income = TransactionIncome(testPositiveAmount, testDate, testDescription)
        justRun { transactionRepository.save(any()) }

        transactionService.transactIncome(income, testUserSignature)

        verify { transactionRepository.save(any()) }
    }

    @Test
    fun `Should return persisted transaction when transactExpense is called`() {
        val expense = TransactionExpense(testNegativeAmount, testDate, testDescription)
        justRun { transactionRepository.save(any()) }

        transactionService.transactExpense(expense, testUserSignature)

        verify { transactionRepository.save(any()) }
    }

    @Test
    fun `Should return correct Balance when calculateBalanceForDate is called with a validDate`() {
        val transactions = listOf(
                Transaction(null, testUserSignature, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME),
                Transaction(null, testUserSignature, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME),
                Transaction(null, testUserSignature, testDate, testNegativeAmount, testDescription, TransactionOperation.EXPENSE)
        )
        every { transactionRepository.findTransactionsBeforeDateByUserSignature(testUserSignature, testDate) } returns transactions

        val result = transactionService.calculateBalanceForDate(testUserSignature, testDate)

        assertEquals(BigDecimal.TEN, result.amount)
        assertEquals(testDate, result.date)

        verify { transactionRepository.findTransactionsBeforeDateByUserSignature(testUserSignature, testDate) }
    }
}