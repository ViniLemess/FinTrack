package br.com.vinilemess.fintrack.transaction

import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

data class Transaction(
    val id: String? = null,
    val amount: BigDecimal,
    val description: String,
    val type: TransactionType,
    val date: LocalDateTime
)

@Serializable
data class CreateTransactionRequest(
    val amount: String,
    val description: String,
    val type: TransactionType,
    val date: String = ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())
)

fun createTransactionFromRequest(createTransactionRequest: CreateTransactionRequest) = Transaction(
    amount = BigDecimal(createTransactionRequest.amount),
    description = createTransactionRequest.description,
    type = createTransactionRequest.type,
    date = LocalDateTime.parse(createTransactionRequest.date, ISO_LOCAL_DATE_TIME)
)

@Serializable
data class TransactionResponse(
    val id: String?,
    val amount: String,
    val description: String,
    val type: TransactionType,
    val date: String,
)

fun Transaction.toResponse() = TransactionResponse(
    id = this.id,
    amount = this.amount.toString(),
    description = this.description,
    type = this.type,
    date = ISO_LOCAL_DATE_TIME.format(this.date)
)
