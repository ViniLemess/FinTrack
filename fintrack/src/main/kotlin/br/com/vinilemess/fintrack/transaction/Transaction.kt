package br.com.vinilemess.fintrack.transaction

import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

data class Transaction(
    val transactionSignature: String,
    val amount: BigDecimal,
    val description: String,
    val type: TransactionType,
    val date: LocalDateTime
)

@Serializable
data class CreateTransactionRequest(
    val transactionSignature: String,
    val amount: String,
    val description: String,
    val type: TransactionType,
    val date: String = ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())
)

fun createTransactionFromRequest(createTransactionRequest: CreateTransactionRequest) = Transaction(
    transactionSignature = createTransactionRequest.transactionSignature,
    amount = BigDecimal(createTransactionRequest.amount),
    description = createTransactionRequest.description,
    type = createTransactionRequest.type,
    date = LocalDateTime.parse(createTransactionRequest.date, ISO_LOCAL_DATE_TIME)
)

@Serializable
data class TransactionResponse(
    val transactionSignature: String,
    val amount: String,
    val description: String,
    val type: TransactionType,
    val date: String,
)

fun Transaction.toResponse() = TransactionResponse(
    transactionSignature = this.transactionSignature,
    amount = this.amount.toString(),
    description = this.description,
    type = this.type,
    date = ISO_LOCAL_DATE_TIME.format(this.date)
)
