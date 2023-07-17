package org.fundatec.vinilemess.tcc.fintrack.balance.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.fundatec.vinilemess.tcc.fintrack.*
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.TransactionResponse
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrack.transaction.service.TransactionService
import org.fundatec.vinilemess.tcc.fintrack.user.service.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class BalanceServiceTest {

    private val transactionService: TransactionService = mockk()
    private val userService: UserService = mockk()
    private val balanceService = BalanceService(transactionService, userService)

    @Test
    fun `Should return correct Balance when calculateBalanceForDate extracts positive amount`() {
        val transactions = listOf(
            TransactionResponse(null, testUserSignatureWithBalance, null, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME),
            TransactionResponse(null, testUserSignatureWithBalance, null, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME),
            TransactionResponse(null, testUserSignatureWithBalance, null, testDate, testNegativeAmount, testDescription, TransactionOperation.EXPENSE)
        )
        every { transactionService.listTransactionsBeforeDateByUserSignature(testUserSignatureWithBalance, testDate) } returns transactions

        val result = balanceService.calculateBalanceForDate(testUserSignatureWithBalance, testDate)

        assertEquals(BigDecimal.TEN, result.amount)
        assertEquals(testDate, result.date)

        verify { transactionService.listTransactionsBeforeDateByUserSignature(testUserSignatureWithBalance, testDate) }
    }

    @Test
    fun `Should return negative Balance when calculateBalanceForDate extracts negative amount`() {
        val transactions = listOf(
            TransactionResponse(null, testUserSignatureWithBalance, null, testDate, testPositiveAmount, testDescription, TransactionOperation.INCOME),
            TransactionResponse(null, testUserSignatureWithBalance, null, testDate, testNegativeAmount, testDescription, TransactionOperation.INCOME),
            TransactionResponse(null, testUserSignatureWithBalance, null, testDate, testNegativeAmount, testDescription, TransactionOperation.EXPENSE)
        )
        every { transactionService.listTransactionsBeforeDateByUserSignature(testUserSignatureWithBalance, testDate) } returns transactions

        val result = balanceService.calculateBalanceForDate(testUserSignatureWithBalance, testDate)

        assertEquals(testNegativeAmount, result.amount)
        assertEquals(testDate, result.date)

        verify { transactionService.listTransactionsBeforeDateByUserSignature(testUserSignatureWithBalance, testDate) }
    }

    @Test
    fun `Should return zero Balance when calculateBalanceForDate is called with no transactions till informed the date`() {
        every { transactionService.listTransactionsBeforeDateByUserSignature(testUserSignatureWithBalance, testDate) } returns listOf()

        val result = balanceService.calculateBalanceForDate(testUserSignatureWithBalance, testDate)

        assertEquals(BigDecimal.ZERO, result.amount)
        assertEquals(testDate, result.date)

        verify { transactionService.listTransactionsBeforeDateByUserSignature(testUserSignatureWithBalance, testDate) }
    }
}