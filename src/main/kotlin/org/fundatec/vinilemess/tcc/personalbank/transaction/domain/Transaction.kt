package org.fundatec.vinilemess.tcc.personalbank.transaction.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDate

@Document(collection = "transactions")
data class Transaction(
    @Id val id: String?,
    val userIdentifier: String,
    val date: LocalDate,
    val amount: BigDecimal
)