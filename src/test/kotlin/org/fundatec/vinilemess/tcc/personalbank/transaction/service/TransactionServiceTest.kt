package org.fundatec.vinilemess.tcc.personalbank.transaction.service

import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.request.TransactionExpense
import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.request.TransactionIncome
import org.fundatec.vinilemess.tcc.personalbank.transaction.repository.TransactionRepository
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class TransactionServiceTest {

    private val transactionRepository: TransactionRepository = mockk()
    private val transactionService = TransactionService(transactionRepository)
    private val testDate = LocalDate.of(2023, 4, 13)
    private val testUserIdentifier = UUID.randomUUID().toString()
    private val testPositiveAmount = BigDecimal.TEN
    private val testNegativeAmount = BigDecimal.TEN.negate()

    @Test
    fun `Should return persisted transaction when transactIncome is called`() {
        val income = TransactionIncome(testPositiveAmount, testDate)
        justRun { transactionRepository.save(any()) }

        transactionService.transactIncome(income, testUserIdentifier)

        verify { transactionRepository.save(any()) }
    }

    @Test
    fun `Should return persisted transaction when transactExpense is called`() {
        val expense = TransactionExpense(testNegativeAmount, testDate)
        justRun { transactionRepository.save(any()) }

        transactionService.transactExpense(expense, testUserIdentifier)

        verify { transactionRepository.save(any()) }
    }
}