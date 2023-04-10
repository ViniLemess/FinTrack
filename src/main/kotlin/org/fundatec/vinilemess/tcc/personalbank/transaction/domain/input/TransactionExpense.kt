package org.fundatec.vinilemess.tcc.personalbank.transaction.domain.input

import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.TransactionOperation
import java.math.BigDecimal
import java.time.LocalDate

class TransactionExpense(
    var amount: BigDecimal,
    var expenseDate: LocalDate
) {
    private val transactionOperation = TransactionOperation.EXPENSE
}