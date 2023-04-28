package org.fundatec.vinilemess.tcc.fintrack.validation

import org.apache.logging.log4j.util.Strings
import org.fundatec.vinilemess.tcc.fintrack.exception.InvalidBodyException
import java.util.Objects

class DataValidator {

    private val violations = mutableMapOf<String, String>()

    fun addCustomConstraint(validation: () -> Boolean, field: String, message: String): DataValidator {
        if (validation()) violations[field] = message
        return this
    }

    fun addNotBlankConstraint(fieldValue: String, field: String): DataValidator {
        if (Strings.isBlank(fieldValue)) violations[field] = "$field cannot be blank"
        return this
    }

    fun addNotNullConstraint(fieldValue: Any?, field: String): DataValidator {
        if (Objects.isNull(fieldValue)) violations[field] = "$field cannot be null"
        return this
    }

    fun validate(errorMessage: String = "The provided data contains violations that must be corrected") {
        if (violations.isNotEmpty()) throw InvalidBodyException(errorMessage, violations)
    }
}