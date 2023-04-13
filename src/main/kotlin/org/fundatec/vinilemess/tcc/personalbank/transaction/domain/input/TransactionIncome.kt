package org.fundatec.vinilemess.tcc.personalbank.transaction.domain.input

import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.TransactionOperation
import org.fundatec.vinilemess.tcc.personalbank.validation.FieldValidator
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class TransactionIncome(
    val amount: BigDecimal,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val incomeDate: LocalDate
) {
    private val transactionOperation = TransactionOperation.INCOME

    fun validateRequest() {
        FieldValidator()
            .addConstraint(isAmountNegative(), "amount", "Expenses cannot be negative or 0")
            .validate()
    }

    fun isAmountNegative() = { -> Objects.isNull(amount) || amount < BigDecimal.ZERO }
}