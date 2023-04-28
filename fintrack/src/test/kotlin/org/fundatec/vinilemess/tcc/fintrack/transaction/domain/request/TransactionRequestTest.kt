package org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request

import org.fundatec.vinilemess.tcc.fintrack.assertProblemDetailsContainsViolation
import org.fundatec.vinilemess.tcc.fintrack.exception.InvalidBodyException
import org.fundatec.vinilemess.tcc.fintrack.testDate
import org.fundatec.vinilemess.tcc.fintrack.testDescription
import org.fundatec.vinilemess.tcc.fintrack.testPositiveAmount
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.TransactionOperation
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class TransactionRequestTest {

    @Test
    fun `Should not throw exception when method validate is called and TransactionRequest is valid`() {
        val transactionRequest = TransactionRequest(testPositiveAmount, testDate, testDescription, TransactionOperation.INCOME)

        assertDoesNotThrow { transactionRequest.validateRequest() }
    }

    @Test
    fun `Should throw exception when validate is called for Transaction with amount 0`() {
        val transactionRequest = TransactionRequest(BigDecimal.ZERO, testDate, testDescription, TransactionOperation.INCOME)
        val exception = assertThrows(InvalidBodyException::class.java) {
            transactionRequest.validateRequest()
        }
        val problemDetail = exception.body
        assertProblemDetailsContainsViolation(problemDetail, "amount")
    }
}