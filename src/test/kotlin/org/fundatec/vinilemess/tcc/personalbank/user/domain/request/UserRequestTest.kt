package org.fundatec.vinilemess.tcc.personalbank.user.domain.request

import org.assertj.core.api.Assertions
import org.fundatec.vinilemess.tcc.personalbank.assertContainsViolation
import org.fundatec.vinilemess.tcc.personalbank.infra.exception.InvalidBodyException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserRequestTest {

    private val testEmail = "test@mail.com"
    private val testName = "tester"

    @Test
    fun `Should not throw exception when validate is called with UserRequest containing valid fields`() {
        val request = UserRequest(testName, testEmail)
        assertDoesNotThrow {
            request.validateRequest()
        }
    }

    @Test
    fun `Should throw exception when validate is called with UserRequest containing blank name`() {
        val request = UserRequest("", testEmail)
        val exception = assertThrows(InvalidBodyException::class.java) {
            request.validateRequest()
        }
        val problemDetail = exception.body
        Assertions.assertThat { ->
            assertEquals(400, problemDetail.status)
            assertEquals("Invalid user json body, violations must be corrected", problemDetail.detail)
            assertEquals("Invalid request body", problemDetail.title)
            assertNotNull(problemDetail.properties)
            problemDetail.properties?.let { assertContainsViolation(it, "name") }
        }
    }

    @Test
    fun `Should throw exception when validate is called with UserRequest containing blank email`() {
        val request = UserRequest(testName, "")
        val exception = assertThrows(InvalidBodyException::class.java) {
            request.validateRequest()
        }
        val problemDetail = exception.body
        Assertions.assertThat { ->
            assertEquals(400, problemDetail.status)
            assertEquals("Invalid user json body, violations must be corrected", problemDetail.detail)
            assertEquals("Invalid request body", problemDetail.title)
            assertNotNull(problemDetail.properties)
            problemDetail.properties?.let { assertContainsViolation(it, "email") }
        }
    }

    @Test
    fun `Should throw exception when validate is called with UserRequest containing invalid email`() {
        val request = UserRequest(testName, "")
        val exception = assertThrows(InvalidBodyException::class.java) {
            request.validateRequest()
        }
        val problemDetail = exception.body
        Assertions.assertThat { ->
            assertEquals(400, problemDetail.status)
            assertEquals("Invalid user json body, violations must be corrected", problemDetail.detail)
            assertEquals("Invalid request body", problemDetail.title)
            assertNotNull(problemDetail.properties)
            problemDetail.properties?.let { assertContainsViolation(it, "email") }
        }
    }
}