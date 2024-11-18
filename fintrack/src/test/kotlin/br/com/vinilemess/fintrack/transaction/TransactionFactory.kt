package br.com.vinilemess.fintrack.transaction

import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

val testDate: LocalDateTime = LocalDateTime.parse("2034-12-03T10:15:30", ISO_LOCAL_DATE_TIME)

fun defaultTransactionInfo(
    id: Long = 1,
    description: String = "test transaction",
    date: LocalDateTime = testDate,
    type: TransactionType = TransactionType.INCOME,
    amount: BigDecimal = BigDecimal.TEN
) = TransactionInfo(
    id = id,
    description = description,
    date = date,
    type = type,
    amount = amount
)

fun defaultCreateTransactionRequest(
    description: String = "test transaction",
    date: LocalDateTime = testDate,
    type: TransactionType = TransactionType.INCOME,
    amount: BigDecimal = BigDecimal.TEN
) = CreateTransactionRequest(
    description = description,
    date = date,
    type = type,
    amount = amount
)