package br.com.vinilemess.fintrack.plugins

import br.com.vinilemess.fintrack.ProblemDetail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.swagger.codegen.v3.generators.html.StaticHtmlCodegen
import java.net.URI

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ProblemDetail(
                    title = "Invalid parameters",
                    status = 400,
                    detail = "Invalid parameters informed",
                    instance = URI.create("/"),
                    properties = emptyMap()
                )
            )
        }
        openAPI(
            path = "openapi",
            swaggerFile = "openapi/documentation.yaml"
        ) {
            codegen = StaticHtmlCodegen()
        }
    }
}
