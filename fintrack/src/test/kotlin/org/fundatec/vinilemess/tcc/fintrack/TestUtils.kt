package org.fundatec.vinilemess.tcc.fintrack

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.springframework.http.ProblemDetail

private const val defaultMessage = "The provided data contains violations that must be corrected"

fun assertProblemDetailsContainsViolation(problemDetail: ProblemDetail, field: String, msg: String = defaultMessage) {
    assertThat { ->
        Assertions.assertEquals(400, problemDetail.status)
        Assertions.assertEquals(msg, problemDetail.detail)
        Assertions.assertEquals("Invalid request body", problemDetail.title)
        Assertions.assertNotNull(problemDetail.properties)
        problemDetail.properties?.let { assertContainsViolation(it, field) }
    }
}

private fun assertContainsViolation(properties: Map<String, Any>, violation: String) {
    properties["invalidFields"]?.let { invalidFields: Any ->
        if ((invalidFields is Map<*, *>) && invalidFields.all { it.key is String && it.value is String }) {
            assertTrue(invalidFields.containsKey(violation)) { -> "no value found for violation key $violation" }
        } else {
            fail("could not apply cast to Map<String, String> for $invalidFields")
        }
    }
}