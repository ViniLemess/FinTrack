package org.fundatec.vinilemess.fintrack.security.contract.response

data class LoginResponse(
        val name: String,
        val email: String,
        val userSignature: String,
        val token: String,
        val refreshToken: String,
        val role: String
)