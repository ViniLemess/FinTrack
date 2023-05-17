package org.fundatec.vinilemess.tcc.fintrackapp.data

data class Transaction(
    val id: String,
    val date: String,
    val amount: Double,
    val description: String?,
    val transactionOperation: TransactionOperation
)