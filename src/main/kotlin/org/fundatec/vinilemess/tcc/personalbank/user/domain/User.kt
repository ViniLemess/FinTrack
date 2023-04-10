package org.fundatec.vinilemess.tcc.personalbank.user.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    val id: String?,
    val name: String,
    val email: String,
    val userIdentifier: String
)