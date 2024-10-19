package br.com.vinilemess.fintrack.transaction

import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

val testDate: LocalDateTime = LocalDateTime.parse("2034-12-03T10:15:30", ISO_LOCAL_DATE_TIME)

fun defaultTransaction(
    transactionSignature: String = "transactionSignature",
    description: String = "test transaction",
    date: LocalDateTime = testDate,
    type: TransactionType = TransactionType.INCOME,
    amount: BigDecimal = BigDecimal.TEN
) = Transaction(
    transactionSignature = transactionSignature,
    description = description,
    date = date,
    type = type,
    amount = amount
)

fun defaultTransactionResponse(
    transactionSignature: String = "transactionSignature",
    description: String = "test transaction",
    date: LocalDateTime = testDate,
    type: TransactionType = TransactionType.INCOME,
    amount: String = BigDecimal.TEN.toString()
) = defaultTransaction(
    transactionSignature = transactionSignature,
    description = description,
    date = date,
    type = type,
    amount = BigDecimal(amount)
).toResponse()


fun defaultCreateTransactionRequest(
    transactionSignature: String = "transactionSignature",
    description: String = "test transaction",
    date: LocalDateTime = testDate,
    type: TransactionType = TransactionType.INCOME,
    amount: BigDecimal = BigDecimal.TEN
) = CreateTransactionRequest(
    transactionSignature = transactionSignature,
    description = description,
    date = date,
    type = type,
    amount = amount
)