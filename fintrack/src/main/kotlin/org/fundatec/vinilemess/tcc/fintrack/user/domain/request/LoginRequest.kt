package org.fundatec.vinilemess.tcc.fintrack.user.domain.request

import org.apache.logging.log4j.util.Strings
import org.fundatec.vinilemess.tcc.fintrack.validation.DataValidator

private const val EMAIL_REGEX = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
private const val IDIOT_PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"

data class LoginRequest(val email: String, val password: String) {

    fun validateRequest() {
        DataValidator()
            .addCustomConstraint(isEmailValid(), "email", "Invalid email")
            .addCustomConstraint(isPasswordValid(), "password", "Password must contain at leats 1 letter, 1 digit and have 8 characters")
            .validate("Invalid login json body, violations must be corrected")
    }

    private fun isEmailValid() = { -> Strings.isBlank(email) || !email.matches(Regex(EMAIL_REGEX)) }
    private fun isPasswordValid() = { -> Strings.isBlank(password) || !password.matches(Regex(IDIOT_PASSWORD_REGEX)) }
}