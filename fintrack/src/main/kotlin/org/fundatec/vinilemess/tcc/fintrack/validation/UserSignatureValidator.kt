package org.fundatec.vinilemess.tcc.fintrack.validation

fun validateUserSignature(userSignature: String) {
    DataValidator()
        .addNotBlankConstraint(userSignature, "userSignature")
        .validate()
}
