package org.fundatec.vinilemess.tcc.personalbank

import org.junit.jupiter.api.Assertions

fun assertContainsViolation(properties: Map<String, Any>, violation: String) {
    properties["invalidFields"]?.let { invalidFields: Any ->
        if ((invalidFields is Map<*, *>) && invalidFields.all { it.key is String && it.value is String }) {
            Assertions.assertTrue(invalidFields.containsKey(violation)) { -> "no value found for violation key $violation" }
        } else {
            Assertions.fail("could not apply cast to Map<String, String> for $invalidFields")
        }
    }
}