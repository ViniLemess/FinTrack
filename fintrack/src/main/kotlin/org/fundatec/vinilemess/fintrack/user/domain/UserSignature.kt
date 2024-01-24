package org.fundatec.vinilemess.fintrack.user.domain

import org.fundatec.vinilemess.fintrack.validation.DataValidator

class UserSignature(val userSignature: String) {
    init {
        validateUserSignature()
    }

    private fun validateUserSignature() {
        DataValidator()
                .addNotBlankConstraint(userSignature, "userSignature")
                .validate()
    }
}

