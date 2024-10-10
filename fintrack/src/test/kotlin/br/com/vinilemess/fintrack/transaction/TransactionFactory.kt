package br.com.vinilemess.fintrack.transaction

import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

fun defaultTransaction(
    id: String = "transactionId",
    description: String = "test transaction",
    date: LocalDateTime = LocalDateTime.MAX,
    type: TransactionType = TransactionType.INCOME,
    amount: BigDecimal = BigDecimal.TEN
) = Transaction(
    id = id,
    description = description,
    date = date,
    type = type,
    amount = amount
)

fun defaultTransactionResponse(
    id: String = "transactionId",
    description: String = "test transaction",
    date: String = ISO_LOCAL_DATE_TIME.format(LocalDateTime.MAX),
    type: TransactionType = TransactionType.INCOME,
    amount: String = BigDecimal.TEN.toString()
) = defaultTransaction(
    id = id,
    description = description,
    date = LocalDateTime.parse(date, ISO_LOCAL_DATE_TIME),
    type = type,
    amount = BigDecimal(amount)
).toResponse()

fun defaultCreateTransactionRequest(
    description: String = "test transaction",
    date: String = ISO_LOCAL_DATE_TIME.format(LocalDateTime.MAX),
    type: TransactionType = TransactionType.INCOME,
    amount: String = BigDecimal.TEN.toString()
) = CreateTransactionRequest(description = description, date = date, type = type, amount = amount)