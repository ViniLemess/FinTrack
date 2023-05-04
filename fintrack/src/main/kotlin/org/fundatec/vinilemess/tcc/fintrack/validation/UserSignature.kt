package org.fundatec.vinilemess.tcc.fintrack.validation

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

