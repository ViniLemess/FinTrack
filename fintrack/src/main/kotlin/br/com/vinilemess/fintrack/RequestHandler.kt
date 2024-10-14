package br.com.vinilemess.fintrack

import br.com.vinilemess.fintrack.error.ErrorResponseException
import br.com.vinilemess.fintrack.error.ProblemDetail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("RequestHandler")

suspend fun PipelineContext<Unit, ApplicationCall>.handleRequest(
    handler: suspend () -> Any,
    status: HttpStatusCode = HttpStatusCode.OK
) {
    runCatching {
        call.respond(
            status = status,
            message = handler()
        )
    }.onFailure {
        if (it is ErrorResponseException) {
            val problemDetail = it.problemDetail
            problemDetail.instance = call.request.path()
            call.respond(
                status = HttpStatusCode.fromValue(problemDetail.status),
                message = problemDetail
            )
        } else {
            logger.error("Error executing request for ${call.request.path()}", it)
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = ProblemDetail(
                    title = "Internal Error",
                    status = 500,
                    detail = "Something went wrong with your request :(",
                    instance = call.request.path()
                )
            )
        }
    }
}