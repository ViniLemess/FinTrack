package org.fundatec.vinilemess.fintrack.data.factory

import org.fundatec.vinilemess.fintrack.transaction.domain.Transaction
import org.fundatec.vinilemess.fintrack.transaction.domain.enums.TransactionOperation
import java.math.BigDecimal
import java.time.LocalDate


fun createIncomeTransactionForSignature(userSignature: String, date: LocalDate = LocalDate.now()): Transaction {
    return Transaction(
            userSignature = userSignature,
            transactionOperation = TransactionOperation.INCOME,
            date = date,
            amount = BigDecimal.TEN,
            id = null,
            recurrenceId = null,
            description = null
    )
}

fun createExpenseTransactionForSignature(userSignature: String, date: LocalDate = LocalDate.now()): Transaction {
    return Transaction(
            userSignature = userSignature,
            transactionOperation = TransactionOperation.EXPENSE,
            date = date,
            amount = BigDecimal.TEN.negate(),
            id = null,
            recurrenceId = null,
            description = null
    )
}
