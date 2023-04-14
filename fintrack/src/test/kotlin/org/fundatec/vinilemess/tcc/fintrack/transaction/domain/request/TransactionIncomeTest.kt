package org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request

import org.assertj.core.api.Assertions
import org.fundatec.vinilemess.tcc.fintrack.assertContainsViolation
import org.fundatec.vinilemess.tcc.fintrack.infra.exception.InvalidBodyException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal
import java.time.LocalDate

class TransactionIncomeTest {
    private val testAmount = BigDecimal.valueOf(5000.0)
    private val testDate = LocalDate.of(2023, 4, 25)
    private val testDescription = "test"

    @Test
    fun `Should not throw exception when validate is called with TransactionIncome containing positive amount`() {
        val request = TransactionIncome(testAmount, testDate, testDescription)
        assertDoesNotThrow {
            request.validateRequest()
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = [-1.0, 0.0])
    fun `Should throw exception when validate is called with TransactionIncome containing negative or 0 amount value`(amountValue: Double) {
        val request = TransactionIncome(BigDecimal.valueOf(amountValue), testDate, testDescription)
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