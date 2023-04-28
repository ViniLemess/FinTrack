package org.fundatec.vinilemess.tcc.fintrack.balance.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.fundatec.vinilemess.tcc.fintrack.*
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.TransactionResponse
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrack.transaction.service.TransactionService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class BalanceServiceTest {

    private val transactionService: TransactionService = mockk()
    private val balanceService = BalanceService(transactionService)

    @Test
    fun `Should return correct Balance when calculateBalanceForDate extracts positive amount`() {
        val transactions = listOf(
            TransactionResponse(null, testUserSignature, null, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME),
            TransactionResponse(null, testUserSignature, null, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME),
            TransactionResponse(null, testUserSignature, null, testDate, testNegativeAmount, testDescription, TransactionOperation.EXPENSE)
        )
        every { transactionService.listTransactionsBeforeDateByUserSignature(testUserSignature, testDate) } returns transactions

        val result = balanceService.calculateBalanceForDate(testUserSignature, testDate)

        assertEquals(BigDecimal.TEN, result.amount)
        assertEquals(testDate, result.date)

        verify { transactionService.listTransactionsBeforeDateByUserSignature(testUserSignature, testDate) }
    }

    @Test
    fun `Should return negative Balance when calculateBalanceForDate extracts negative amount`() {
        val transactions = listOf(
            TransactionResponse(null, testUserSignature, null, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME),
            TransactionResponse(null, testUserSignature, null, testDate, testNegativeAmount, testDescription, TransactionOperation.INCOME),
            TransactionResponse(null, testUserSignature, null, testDate, testNegativeAmount, testDescription, TransactionOperation.EXPENSE)
        )
        every { transactionService.listTransactionsBeforeDateByUserSignature(testUserSignature, testDate) } returns transactions

        val result = balanceService.calculateBalanceForDate(testUserSignature, testDate)

        assertEquals(testNegativeAmount, result.amount)
        assertEquals(testDate, result.date)

        verify { transactionService.listTransactionsBeforeDateByUserSignature(testUserSignature, testDate) }
    }

    @Test
    fun `Should return zero Balance when calculateBalanceForDate is called with no transactions till informed the date`() {
        every { transactionService.listTransactionsBeforeDateByUserSignature(testUserSignature, testDate) } returns listOf()

        val result = balanceService.calculateBalanceForDate(testUserSignature, testDate)

        assertEquals(BigDecimal.ZERO, result.amount)
        assertEquals(testDate, result.date)

        verify { transactionService.listTransactionsBeforeDateByUserSignature(testUserSignature, testDate) }
    }
}