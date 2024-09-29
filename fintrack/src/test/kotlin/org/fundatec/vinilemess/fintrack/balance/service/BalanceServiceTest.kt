package org.fundatec.vinilemess.fintrack.balance.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.fundatec.vinilemess.fintrack.*
import org.fundatec.vinilemess.fintrack.balance.BalanceService
import org.fundatec.vinilemess.fintrack.transaction.contract.TransactionResponse
import org.fundatec.vinilemess.fintrack.transaction.contract.enums.TransactionOperation
import org.fundatec.vinilemess.fintrack.transaction.TransactionService
import org.fundatec.vinilemess.fintrack.user.UserService
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
            TransactionResponse(
                null,
                userSignature,
                null,
                testDate,
                testPositiveAmount,
                testDescription,
                TransactionOperation.INCOME
            ),
            TransactionResponse(
                null,
                userSignature,
                null,
                testDate,
                testPositiveAmount,
                testDescription,
                TransactionOperation.INCOME
            ),
            TransactionResponse(
                null,
                userSignature,
                null,
                testDate,
                testNegativeAmount,
                testDescription,
                TransactionOperation.EXPENSE
            )
        )
        every {
            transactionService.listTransactionsBeforeDateByUserSignature(
                userSignature,
                testDate
            )
        } returns transactions

        every { userService.existsSignature(any()) } returns true

        val result = balanceService.calculateBalanceForDate(userSignature, testDate)

        assertEquals(BigDecimal.TEN, result.amount)
        assertEquals(testDate, result.date)

        verify { transactionService.listTransactionsBeforeDateByUserSignature(userSignature, testDate) }
    }

    @Test
    fun `Should return negative Balance when calculateBalanceForDate extracts negative amount`() {
        val transactions = listOf(
            TransactionResponse(
                null,
                userSignature,
                null,
                testDate,
                testPositiveAmount,
                testDescription,
                TransactionOperation.INCOME
            ),
            TransactionResponse(
                null,
                userSignature,
                null,
                testDate,
                testNegativeAmount,
                testDescription,
                TransactionOperation.INCOME
            ),
            TransactionResponse(
                null,
                userSignature,
                null,
                testDate,
                testNegativeAmount,
                testDescription,
                TransactionOperation.EXPENSE
            )
        )
        every {
            transactionService.listTransactionsBeforeDateByUserSignature(
                userSignature,
                testDate
            )
        } returns transactions

        every { userService.existsSignature(any()) } returns true

        val result = balanceService.calculateBalanceForDate(userSignature = userSignature, testDate)

        assertEquals(testNegativeAmount, result.amount)
        assertEquals(testDate, result.date)

        verify { transactionService.listTransactionsBeforeDateByUserSignature(userSignature, testDate) }
    }

    @Test
    fun `Should return zero Balance when calculateBalanceForDate is called with no transactions till informed the date`() {
        every {
            transactionService.listTransactionsBeforeDateByUserSignature(
                userSignature,
                testDate
            )
        } returns listOf()

        every { userService.existsSignature(any()) } returns true

        val result = balanceService.calculateBalanceForDate(userSignature, testDate)

        assertEquals(BigDecimal.ZERO, result.amount)
        assertEquals(testDate, result.date)

        verify { transactionService.listTransactionsBeforeDateByUserSignature(userSignature = userSignature, testDate) }
    }
}