package br.com.vinilemess.fintrack.configuration

data class PostgresProperties(
    val host: String,
    val port: Int = 5432,
    val database: String,
    val username: String,
    val password: String,
)