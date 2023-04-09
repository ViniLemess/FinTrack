package org.fundatec.vinilemess.tcc.personalbank.transaction.domain.input

import jakarta.validation.constraints.Positive
import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.TransactionOperation
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class TransactionIncome(
    @Positive
    val amount: Double,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val incomeDate: LocalDate
) {
    private val transactionOperation = TransactionOperation.INCOME
}