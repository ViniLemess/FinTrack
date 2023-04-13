package org.fundatec.vinilemess.tcc.personalbank.validation

import org.assertj.core.api.Assertions
import org.fundatec.vinilemess.tcc.personalbank.assertContainsViolation
import org.fundatec.vinilemess.tcc.personalbank.infra.exception.InvalidBodyException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FieldValidatorTest {

    private val testField = "testField"
    private val testMessage = "testing"

    @Test
    fun `Should not throw exception when validate is called with violations map empty`() {
        assertDoesNotThrow {
            FieldValidator()
                .addConstraint({ -> false }, testField, testMessage)
                .validate()
        }
    }

    @Test
    fun `Should throw exception when validate is called with violations map not empty`() {
        val exception = assertThrows(InvalidBodyException::class.java) {
            FieldValidator()
                .addConstraint({ -> true }, testField, testMessage)
                .validate()
        }
        val problemDetail = exception.body
        Assertions.assertThat { ->
            assertEquals(400, problemDetail.status)
            assertEquals("Fields contains violations that must be corrected", problemDetail.detail)
            assertEquals("Invalid request body", problemDetail.title)
            assertNotNull(problemDetail.properties)
            problemDetail.properties?.let { assertContainsViolation(it, testField) }
        }
    }
}