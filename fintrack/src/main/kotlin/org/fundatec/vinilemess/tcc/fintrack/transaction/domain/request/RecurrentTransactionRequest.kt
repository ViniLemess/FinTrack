package org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request

import org.fundatec.vinilemess.tcc.fintrack.infra.Request
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
) : Request {
    override fun validateRequest() {
        DataValidator()
            .addCustomConstraint(isAmountNullOrZero(), "amount", "amount cannot be null or 0")
            .addNotNullConstraint(operation, "operation")
            .addNotNullConstraint(recurrence, "recurrence")
            .addCustomConstraint(isFrequencyNegative(), "frequency", "Frequency cannot be 0 or negative")
            .addCustomConstraint(isRecurrenceCountNegative(), "recurrenceCount", "Recurrence cannot be 0 or negative")
            .addCustomConstraint(isRecurrenceCountBiggerThenAllowed(), "recurrenceCount", "Recurrence is to big")
            .validate()
    }

    private fun isAmountNullOrZero() = { -> Objects.isNull(amount) || amount == BigDecimal.ZERO }

    private fun isFrequencyNegative() = { -> Objects.isNull(frequency) || frequency <= 0 }

    private fun isRecurrenceCountNegative() = { -> Objects.isNull(recurrenceCount) || recurrenceCount <= 0 }

    private fun isRecurrenceCountBiggerThenAllowed() = { ->
        Objects.isNull(recurrenceCount) || when (recurrence) {
            Recurrence.DAILY -> recurrenceCount > 30
            Recurrence.WEEKLY -> recurrenceCount > 15
            Recurrence.MONTHLY, Recurrence.YEARLY -> recurrenceCount > 12
        }
    }
}