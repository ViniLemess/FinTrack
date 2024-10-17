package br.com.vinilemess.fintrack.configuration

import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.routing.*
import io.swagger.codegen.v3.generators.html.StaticDocCodegen

fun Application.configureSwagger() {
    routing {
        openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml") {
            codegen = StaticDocCodegen()
        }
    }
}
