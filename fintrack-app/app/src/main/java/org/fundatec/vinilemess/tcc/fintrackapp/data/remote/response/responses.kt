package org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response

import java.math.BigDecimal
import java.time.LocalDate

data class UserResponse(
    val name: String,
    val email: String,
    val userSignature: String
)

data class TransactionResponse(
    val id: String,
    val userSignature: String,
    val recurrenceId: String?,
    val date: String,
    val amount: Double,
    val description: String?,
    val transactionOperation: String
)

data class BalanceResponse(val amount: Double, val date: String)
