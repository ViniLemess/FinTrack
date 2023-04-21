package org.fundatec.vinilemess.tcc.fintrack.balance.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.fundatec.vinilemess.tcc.fintrack.balance.repository.BalanceRepository
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.Transaction
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrack.transaction.service.TransactionService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class BalanceServiceTest {

    private val transactionService: TransactionService = mockk()
    private val balanceRepository: BalanceRepository = mockk()
    private val balanceService = BalanceService(transactionService, balanceRepository)
    private val testDate = LocalDate.of(2023, 4, 13)
    private val testUserSignature = UUID.randomUUID().toString()
    private val testPositiveAmount = BigDecimal.TEN
    private val testNegativeAmount = BigDecimal.TEN.negate()
    private val testDescription = "test"

    @Test
    fun `Should return correct Balance when calculateBalanceForDate is called with a valid date`() {
        val transactions = listOf(
            Transaction(null, testUserSignature, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME),
            Transaction(null, testUserSignature, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME),
            Transaction(null, testUserSignature, testDate, testNegativeAmount, testDescription, TransactionOperation.EXPENSE)
        )
        every { transactionService.getTransactionsBeforeDateByUserSignature(testUserSignature, testDate) } returns transactions

        val result = balanceService.calculateBalanceForDate(testUserSignature, testDate)

        assertEquals(BigDecimal.TEN, result.amount)
        assertEquals(testDate, result.date)

        verify { transactionService.getTransactionsBeforeDateByUserSignature(testUserSignature, testDate) }
    }
}