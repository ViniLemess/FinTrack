package br.com.vinilemess.fintrack.common

import br.com.vinilemess.fintrack.common.ApiResult.Failure
import br.com.vinilemess.fintrack.common.ApiResult.Success
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("RequestHandler")

suspend fun PipelineContext<Unit, ApplicationCall>.handleRequest(
    handler: suspend () -> ApiResult<Any>,
    status: HttpStatusCode = HttpStatusCode.OK
) {
    runCatching {
        when (val result = handler()) {
            is Success -> handleSuccess(status, result)
            is Failure -> handleFailure(result)
        }
    }.onFailure {
        logger.error("Error executing ${call.request.httpMethod} request for ${call.request.path()}", it)
        when (it) {
            is BadRequestException -> call.respond(
                status = HttpStatusCode.BadRequest,
                message = ProblemDetail(
                    title = "Bad Request",
                    status = 400,
                    detail = "Invalid request, make sure to inform the correct data",
                    instance = call.request.path()
                )
            )
            else -> call.respond(
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

private suspend fun PipelineContext<Unit, ApplicationCall>.handleFailure(result: Failure) {
    call.respond(
        status = HttpStatusCode.fromValue(result.problemDetail.status),
        message = result.problemDetail.copy(instance = call.request.path())
    )
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleSuccess(
    status: HttpStatusCode,
    result: Success<Any>
) {
    call.respond(status = status, message = result.value)
}