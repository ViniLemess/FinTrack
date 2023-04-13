package org.fundatec.vinilemess.tcc.personalbank.user.domain.request

import org.apache.logging.log4j.util.Strings
import org.fundatec.vinilemess.tcc.personalbank.validation.FieldValidator

private const val EMAIL_REGEX = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"

class UserRequest(
    val name: String,
    val email: String
) {
    fun validateRequest() {
        FieldValidator()
            .addConstraint(isNameBlank(), "name", "Cannot be blank")
            .addConstraint(isEmailValid(), "email", "Invalid email")
            .validate("Invalid user json body, violations must be corrected")
    }

    private fun isNameBlank() = { -> Strings.isBlank(name) }

    private fun isEmailValid() = { -> Strings.isBlank(email) || !email.matches(Regex(EMAIL_REGEX)) }

}