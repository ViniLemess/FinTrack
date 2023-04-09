package org.fundatec.vinilemess.tcc.personalbank.transaction.domain.input

import jakarta.validation.constraints.Negative
import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.TransactionOperation
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class TransactionExpense(
    @Negative
    val amount: Double,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val expenseDate: LocalDate
) {
    private val transactionOperation = TransactionOperation.EXPENSE
}