package br.com.vinilemess.fintrack

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
import br.com.vinilemess.fintrack.transaction.TransactionController

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    di { import(Modules.appModule) }
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

private fun Routing.configureSwagger() {
    openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml") {
        codegen = StaticDocCodegen()
    }
}

private fun Routing.registerControllerRoutes() {
    val transactionController: TransactionController by closestDI().instance<TransactionController>()
    transactionController.registerRoutes(this)
}
