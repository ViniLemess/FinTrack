package br.com.vinilemess.fintrack

import br.com.vinilemess.fintrack.configuration.PostgresProperties
import br.com.vinilemess.fintrack.configuration.configureDatabaseTables
import br.com.vinilemess.fintrack.configuration.configureSwagger
import br.com.vinilemess.fintrack.transaction.configureTransactionRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.kodein.di.ktor.di

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val postgresProperties = initializePostgresProperties()
    di { import(Modules.initializeDependencies(postgresProperties)) }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    configureSwagger()
    configureTransactionRouting()
    configureDatabaseTables()
}

private fun Application.initializePostgresProperties(): PostgresProperties {
    return environment.config.let { config ->
        PostgresProperties(
            host = config.property("postgres.host").getString(),
            port = config.propertyOrNull("postgres.port")?.getString()?.toInt() ?: 5432,
            database = config.property("postgres.database").getString(),
            username = config.property("postgres.username").getString(),
            password = config.property("postgres.password").getString()
        )
    }
}
