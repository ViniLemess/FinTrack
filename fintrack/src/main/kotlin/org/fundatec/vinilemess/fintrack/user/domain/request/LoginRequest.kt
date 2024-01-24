package org.fundatec.vinilemess.fintrack.user.domain.request

import org.apache.logging.log4j.util.Strings
import org.fundatec.vinilemess.fintrack.validation.DataValidator

private const val EMAIL_REGEX = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"

data class LoginRequest(val email: String, val password: String) {

    fun validateRequest() {
        DataValidator()
            .addCustomConstraint(isEmailValid(), "email", "Invalid email")
            .addNotBlankConstraint(password, "password")
            .validate("Invalid login json body, violations must be corrected")
    }

    private fun isEmailValid() = { -> Strings.isBlank(email) || !email.matches(Regex(EMAIL_REGEX)) }
}