package org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request

import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrack.validation.FieldValidator
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class TransactionIncome(
        override val amount: BigDecimal,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        override val date: LocalDate,
        override val description: String?
) : TransactionRequest {
    override val operation = TransactionOperation.INCOME

    override fun validateRequest() {
        FieldValidator()
                .addConstraint(isAmountNegative(), "amount", "Expenses cannot be negative or 0")
                .validate()
    }

    private fun isAmountNegative() = { -> Objects.isNull(amount) || amount <= BigDecimal.ZERO }
}