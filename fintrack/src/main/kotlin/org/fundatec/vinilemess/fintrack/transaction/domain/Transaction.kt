package org.fundatec.vinilemess.fintrack.transaction.domain

import com.fasterxml.jackson.annotation.JsonInclude
import org.fundatec.vinilemess.fintrack.transaction.domain.enums.TransactionOperation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDate

@Document(collection = "transactions")
data class Transaction(
    @Id val id: String?,
    val userSignature: String,
    val recurrenceId: String?,
    val date: LocalDate,
    val amount: BigDecimal,
    val description: String?,
    val transactionOperation: TransactionOperation
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TransactionResponse(
    val id: String?,
    val userSignature: String,
    val recurrenceId: String?,
    val date: LocalDate,
    val amount: BigDecimal,
    val description: String?,
    val transactionOperation: TransactionOperation
)

fun Transaction.toResponse() = TransactionResponse(
    id = id,
    userSignature = userSignature,
    recurrenceId = recurrenceId,
    date = date,
    amount = amount,
    description = description,
    transactionOperation = transactionOperation
)