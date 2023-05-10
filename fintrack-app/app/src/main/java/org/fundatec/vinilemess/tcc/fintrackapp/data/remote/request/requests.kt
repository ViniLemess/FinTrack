package org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request

import org.fundatec.vinilemess.tcc.fintrackapp.data.Recurrence
import org.fundatec.vinilemess.tcc.fintrackapp.data.TransactionOperation

data class TransactionRequest(
    val amount: Double,
    val date: String,
    val description: String?,
    val operation: String
)

data class UserRequest(
    val name: String,
    val email: String,
    val password: String,
)

class RecurrentTransactionRequest(
    val amount: Double,
    val inititalDate: String,
    val description: String?,
    val operation: TransactionOperation,
    val recurrence: Recurrence,
    val frequency: Long,
    val recurrenceCount: Int
)