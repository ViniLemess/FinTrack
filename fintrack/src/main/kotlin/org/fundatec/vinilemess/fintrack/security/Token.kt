package org.fundatec.vinilemess.fintrack.security

import org.fundatec.vinilemess.fintrack.user.domain.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

private const val BEARER = "Bearer"

@Document(collection = "tokens")
class Token(
        @Id
        val id: String?,
        @Indexed(unique = true)
        val token: String,
        val type: String = BEARER,
        val isExpired: Boolean,
        val isRevoked: Boolean,
        @DBRef(lazy = true)
        val user: User
) {
}