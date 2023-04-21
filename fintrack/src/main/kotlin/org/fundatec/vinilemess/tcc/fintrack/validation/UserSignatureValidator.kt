package org.fundatec.vinilemess.tcc.fintrack.infra.validation

fun validateUserSignature(userSignature: String) {
    DataValidator()
        .addNotBlankConstraint(userSignature, "userSignature")
        .validate()
}
