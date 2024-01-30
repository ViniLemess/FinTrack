package org.fundatec.vinilemess.fintrack.user.domain

import org.fundatec.vinilemess.fintrack.security.Token
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document(collection = "users")
data class User(
        @Id
        var id: String?,
        val name: String,
        val email: String,
        private val password: String,
        val transactionSignature: String,
        val role: Role,
        @DBRef(lazy = true)
        val tokens: Collection<Token>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return this.role.getAuthorities()
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}