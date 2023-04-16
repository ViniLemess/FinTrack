package org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request

import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.Recurrence
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrack.validation.DataValidator
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class RecurrentTransactionRequest(
    val amount: BigDecimal,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val inititalDate: LocalDate,
    val description: String?,
    val operation: TransactionOperation,
    val recurrence: Recurrence,
    val frequency: Long,
    val recurrenceCount: Int
) {
    fun validateRequest() {
        DataValidator()
            .addCustomConstraint(isAmountNullOrZero(), "amount", "amount cannot be null or 0")
            .addNotNullConstraint(operation, "operation")
            .addNotNullConstraint(recurrence, "recurrence")
            .addCustomConstraint(isFrequencyNegative(), "frequency", "Frequency cannot be 0 or negative")
            .addCustomConstraint(isRecurrenceCountNegative(), "recurrenceCount", "Recurrency cannot be 0 or negative")
            .validate()
    }

    private fun isAmountNullOrZero() = { -> Objects.isNull(amount) || amount == BigDecimal.ZERO }

    private fun isFrequencyNegative() = { -> Objects.isNull(frequency) || frequency <= 0 }

    private fun isRecurrenceCountNegative() = { -> Objects.isNull(recurrenceCount) || recurrenceCount <= 0 }
}