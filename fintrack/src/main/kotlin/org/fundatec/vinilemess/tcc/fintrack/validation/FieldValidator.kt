package org.fundatec.vinilemess.tcc.fintrack.validation

import org.fundatec.vinilemess.tcc.fintrack.infra.exception.InvalidBodyException

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