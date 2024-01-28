package org.fundatec.vinilemess.fintrack.validation

import org.fundatec.vinilemess.fintrack.assertProblemDetailsContainsViolation
import org.fundatec.vinilemess.fintrack.exception.InvalidBodyException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class DataValidatorTest {

    private val testField = "testField"
    private val testMessage = "testing"

    @Test
    fun `Should not throw exception when validate is called with violations map empty`() {
        assertDoesNotThrow {
            DataValidator()
                .addCustomConstraint({ -> false }, testField, testMessage)
                .validate()
        }
    }

    @Test
    fun `Should throw exception when validate is called with violations map not empty`() {
        val exception = assertThrows(InvalidBodyException::class.java) {
            DataValidator()
                .addCustomConstraint({ -> true }, testField, testMessage)
                .validate()
        }
        val problemDetail = exception.body
        assertProblemDetailsContainsViolation(problemDetail, testField)
    }

    @Test
    fun `Should throw exception when validate is called with notBlank cosntraint violated`() {
        val exception = assertThrows(InvalidBodyException::class.java) {
            DataValidator()
                .addNotBlankConstraint("", testField)
                .validate()
        }
        val problemDetail = exception.body
        assertProblemDetailsContainsViolation(problemDetail, testField)
    }

    @Test
    fun `Should throw exception when validate is called with notNull cosntraint violated`() {
        val exception = assertThrows(InvalidBodyException::class.java) {
            DataValidator()
                .addNotNullConstraint(null, testField)
                .validate()
        }
        val problemDetail = exception.body
        assertProblemDetailsContainsViolation(problemDetail, testField)
    }


}