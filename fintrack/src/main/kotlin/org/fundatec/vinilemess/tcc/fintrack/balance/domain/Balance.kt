package org.fundatec.vinilemess.tcc.fintrack.balance.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate

@Document(collection = "balances")
data class Balance(
        @Id
        val id: String?,
        val userSignature: String,
        val amount: BigDecimal,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        val date: LocalDate)