package org.fundatec.vinilemess.tcc.fintrack.infra.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException
import java.net.URI
import java.time.LocalDateTime

class InvalidBodyException(errorMessage: String, bodyViolations: Map<String, String>):
    ErrorResponseException(HttpStatus.BAD_REQUEST, asProblemDetail(errorMessage, bodyViolations), null)

private fun asProblemDetail(message: String, bodyViolations: Map<String, String>): ProblemDetail {
    val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message)
    problemDetail.title = "Invalid request body"
    problemDetail.type = URI("https://www.rfc-editor.org/rfc/rfc7807")
    problemDetail.setProperty("timestamp", LocalDateTime.now())
    problemDetail.setProperty("invalidFields", bodyViolations)
    return problemDetail
}