package org.fundatec.vinilemess.tcc.personalbank.transaction.domain.request

import org.assertj.core.api.Assertions
import org.fundatec.vinilemess.tcc.personalbank.assertContainsViolation
import org.fundatec.vinilemess.tcc.personalbank.infra.exception.InvalidBodyException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal
import java.time.LocalDate

class TransactionExpenseTest {

    private val testAmount = BigDecimal.valueOf(-1339.81)
    private val testDate = LocalDate.of(2023, 4, 13)

    @Test
    fun `Should not throw exception when validate is called with TransactionExpense containing negative amount`() {
        val request = TransactionExpense(testAmount, testDate)
        assertDoesNotThrow {
            request.validateRequest()
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, 1.0])
    fun `Should throw exception when validate is called with TransactionExpense containing positive or 0 amount value`(amountValue: Double) {
        val request = TransactionExpense(BigDecimal.valueOf(amountValue), testDate)
        val exception = assertThrows(InvalidBodyException::class.java) {
            request.validateRequest()
        }
        val problemDetail = exception.body
        Assertions.assertThat { ->
            assertEquals(400, problemDetail.status)
            assertEquals("Fields contains violations that must be corrected", problemDetail.detail)
            assertEquals("Invalid request body", problemDetail.title)
            assertNotNull(problemDetail.properties)
            problemDetail.properties?.let { assertContainsViolation(it, "amount") }
        }
    }
}