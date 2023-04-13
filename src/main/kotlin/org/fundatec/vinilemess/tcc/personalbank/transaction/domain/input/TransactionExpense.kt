package org.fundatec.vinilemess.tcc.personalbank.transaction.domain.input

import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.TransactionOperation
import org.fundatec.vinilemess.tcc.personalbank.validation.FieldValidator
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Objects

class TransactionExpense(
    var amount: BigDecimal,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    var expenseDate: LocalDate
) {
    private val transactionOperation = TransactionOperation.EXPENSE

    fun validateRequest() {
        FieldValidator()
            .addConstraint(isAmountPositive(), "amount", "Expenses cannot be positive")
            .validate()
    }

    fun isAmountPositive() = { -> Objects.isNull(amount) || amount > BigDecimal.ZERO }
}