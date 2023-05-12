package org.fundatec.vinilemess.tcc.fintrack.user.domain

import org.fundatec.vinilemess.tcc.fintrack.validation.DataValidator

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

