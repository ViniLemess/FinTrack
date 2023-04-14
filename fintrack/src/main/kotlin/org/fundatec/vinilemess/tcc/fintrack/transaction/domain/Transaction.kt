package org.fundatec.vinilemess.tcc.fintrack.transaction.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDate

@Document(collection = "transactions")
data class Transaction(
    @Id val id: String?,
    val userSignature: String,
    val date: LocalDate,
    val amount: BigDecimal,
    val description: String?,
    val transactionOperation: TransactionOperation
)