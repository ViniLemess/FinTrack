package org.fundatec.vinilemess.tcc.personalbank.transaction.domain.input

import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.TransactionOperation
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate

class TransactionIncome(
    val amount: BigDecimal,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val incomeDate: LocalDate
) {
    private val transactionOperation = TransactionOperation.INCOME
}