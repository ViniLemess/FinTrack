package org.fundatec.vinilemess.tcc.personalbank.validation

import org.fundatec.vinilemess.tcc.personalbank.infra.exception.InvalidBodyException

class FieldValidator {

    private val violations = mutableMapOf<String, String>()

    fun addConstraint(validation: () -> Boolean, field: String, message: String): FieldValidator {
        if (validation()) violations[field] = message
        return this
    }

    fun validate(errorMessage: String = "Fields contains violations that must be corrected") {
        if (violations.isNotEmpty()) throw InvalidBodyException(errorMessage, violations)
    }
}