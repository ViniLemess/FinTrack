package br.com.vinilemess.fintrack

import br.com.vinilemess.fintrack.configuration.MongoProperties
import br.com.vinilemess.fintrack.transaction.TransactionController
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.routing.*
import io.swagger.codegen.v3.generators.html.StaticDocCodegen
import kotlinx.serialization.json.Json
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
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
    routing {
        configureSwagger()
        registerControllerRoutes()
    }
}

private fun Application.initializeMongoProperties() = MongoProperties(
    host = environment.config.property("mongodb.host").getString(),
    database = environment.config.property("mongodb.database").getString(),
    username = environment.config.property("mongodb.username").getString(),
    password = environment.config.property("mongodb.password").getString(),
    authenticateAsAdmin = environment.config.property("mongodb.admin").getString().toBoolean(),
    connectionString = environment.config.propertyOrNull("mongodb.connection-string")?.getString()
)

private fun Routing.configureSwagger() {
    openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml") {
        codegen = StaticDocCodegen()
    }
}

private fun Routing.registerControllerRoutes() {
    val transactionController: TransactionController by closestDI().instance<TransactionController>()
    transactionController.registerRoutes(this)
}
