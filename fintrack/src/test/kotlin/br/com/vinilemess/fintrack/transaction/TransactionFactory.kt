package br.com.vinilemess.fintrack.transaction

import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

const val TEST_DATE = "2034-12-03T10:15:30"

fun defaultTransaction(
    transactionSignature: String = "transactionSignature",
    description: String = "test transaction",
    date: LocalDateTime = LocalDateTime.parse(TEST_DATE, ISO_LOCAL_DATE_TIME),
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
    date: String = TEST_DATE,
    type: TransactionType = TransactionType.INCOME,
    amount: String = BigDecimal.TEN.toString()
) = defaultTransaction(
    transactionSignature = transactionSignature,
    description = description,
    date = LocalDateTime.parse(date, ISO_LOCAL_DATE_TIME),
    type = type,
    amount = BigDecimal(amount)
).toResponse()


fun defaultCreateTransactionRequest(
    transactionSignature: String = "transactionSignature",
    description: String = "test transaction",
    date: String = TEST_DATE,
    type: TransactionType = TransactionType.INCOME,
    amount: String = BigDecimal.TEN.toString()
) = CreateTransactionRequest(
    transactionSignature = transactionSignature,
    description = description,
    date = date,
    type = type,
    amount = amount
)