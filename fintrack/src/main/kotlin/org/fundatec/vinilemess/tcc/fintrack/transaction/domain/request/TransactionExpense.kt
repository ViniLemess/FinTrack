package org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request

import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrack.validation.FieldValidator
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Objects

class TransactionExpense(
        override val amount: BigDecimal,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        override val date: LocalDate,
        override val description: String?
) : TransactionRequest {
    override val operation = TransactionOperation.EXPENSE

    override fun validateRequest() {
        FieldValidator()
                .addConstraint(isAmountPositive(), "amount", "Expenses cannot be positive")
                .validate()
    }

    private fun isAmountPositive() = { -> Objects.isNull(amount) || amount >= BigDecimal.ZERO }
}