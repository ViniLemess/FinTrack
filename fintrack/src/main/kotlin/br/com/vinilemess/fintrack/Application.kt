package br.com.vinilemess.fintrack

import br.com.vinilemess.fintrack.configuration.MongoProperties
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
    val mongoProperties = initializeMongoProperties()
    di { import(Modules.initializeDependencies(mongoProperties)) }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    configureSwagger()
    configureTransactionRouting()
}

private fun Application.initializeMongoProperties() = MongoProperties(
    host = environment.config.property("mongodb.host").getString(),
    database = environment.config.property("mongodb.database").getString(),
    username = environment.config.property("mongodb.username").getString(),
    password = environment.config.property("mongodb.password").getString(),
    authenticateAsAdmin = environment.config.property("mongodb.admin").getString().toBoolean(),
    connectionString = environment.config.propertyOrNull("mongodb.connection-string")?.getString()
)
