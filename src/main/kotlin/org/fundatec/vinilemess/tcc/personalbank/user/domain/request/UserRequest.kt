package org.fundatec.vinilemess.tcc.personalbank.user.domain.request

import org.apache.logging.log4j.util.Strings
import org.fundatec.vinilemess.tcc.personalbank.infra.exception.InvalidBodyException

private const val EMAIL_REGEX = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"

class UserRequest(
    val name: String,
    val email: String
) {
    fun validateRequest() {
        val violations = mutableMapOf<String, String>()

        if (Strings.isBlank(name)) violations["name"] = "Cannot be blank"
        if (Strings.isBlank(email) || !email.matches(Regex(EMAIL_REGEX))) violations["email"] = "Invalid email"

        if (violations.isNotEmpty()) throw InvalidBodyException(
            "Invalid user json body, violations must be corrected",
            violations
        )
    }
}