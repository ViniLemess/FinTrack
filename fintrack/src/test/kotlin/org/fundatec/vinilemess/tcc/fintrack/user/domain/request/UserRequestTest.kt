package org.fundatec.vinilemess.fintrack.user.domain.request

import org.fundatec.vinilemess.fintrack.assertProblemDetailsContainsViolation
import org.fundatec.vinilemess.fintrack.exception.InvalidBodyException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class UserRequestTest {

    private val testEmail = "test@mail.com"
    private val testName = "tester"
    private val testPassword = "idioticpassword123"
    private val customValidationMessage = "Invalid user json body, violations must be corrected"

    @Test
    fun `Should not throw exception when validate is called with UserRequest containing valid fields`() {
        val request = UserRequest(testName, testEmail, testPassword)
        assertDoesNotThrow {
            request.validateRequest()
        }
    }

    @Test
    fun `Should throw exception when validate is called with UserRequest containing blank name`() {
        val request = UserRequest("", testEmail, testPassword)
        val exception = assertThrows(InvalidBodyException::class.java) {
            request.validateRequest()
        }
        val problemDetail = exception.body
        assertProblemDetailsContainsViolation(problemDetail, "name", customValidationMessage)
    }

    @Test
    fun `Should throw exception when validate is called with UserRequest containing blank email`() {
        val request = UserRequest(testName, "", testPassword)
        val exception = assertThrows(InvalidBodyException::class.java) {
            request.validateRequest()
        }
        val problemDetail = exception.body
        assertProblemDetailsContainsViolation(problemDetail, "email", customValidationMessage)
    }

    @Test
    fun `Should throw exception when validate is called with UserRequest containing invalid email`() {
        val request = UserRequest(testName, "testemail.com", testPassword)
        val exception = assertThrows(InvalidBodyException::class.java) {
            request.validateRequest()
        }
        val problemDetail = exception.body
        assertProblemDetailsContainsViolation(problemDetail, "email", customValidationMessage)
    }

    @Test
    fun `Should throw exception when validate is called with UserRequest containing invalid password`() {
        val request = UserRequest(testName, testEmail, "123")
        val exception = assertThrows(InvalidBodyException::class.java) {
            request.validateRequest()
        }
        val problemDetail = exception.body
        assertProblemDetailsContainsViolation(problemDetail, "password", customValidationMessage)
    }
}