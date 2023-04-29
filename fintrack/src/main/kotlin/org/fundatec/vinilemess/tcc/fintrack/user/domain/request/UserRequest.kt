package org.fundatec.vinilemess.tcc.fintrack.user.domain.request

import org.apache.logging.log4j.util.Strings
import org.fundatec.vinilemess.tcc.fintrack.infra.Request
import org.fundatec.vinilemess.tcc.fintrack.validation.DataValidator

private const val EMAIL_REGEX = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
private const val IDIOT_PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"

class UserRequest(
    val name: String,
    val email: String,
    val password: String,
) : Request {
    override fun validateRequest() {
        DataValidator()
            .addNotBlankConstraint(name, "name")
            .addCustomConstraint(isEmailValid(), "email", "Invalid email")
            .addCustomConstraint(
                isPasswordValid(),
                "password",
                "Password must contain at leats 1 letter, 1 digit and have 8 characters"
            )
            .validate("Invalid user json body, violations must be corrected")
    }

    private fun isEmailValid() = { -> Strings.isBlank(email) || !email.matches(Regex(EMAIL_REGEX)) }
    private fun isPasswordValid() = { -> Strings.isBlank(password) || !password.matches(Regex(IDIOT_PASSWORD_REGEX)) }
}