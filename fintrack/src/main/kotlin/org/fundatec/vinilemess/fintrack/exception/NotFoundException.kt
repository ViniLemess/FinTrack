package org.fundatec.vinilemess.fintrack.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException
import java.net.URI
import java.time.LocalDateTime

class NotFoundException(errorMessage: String):
    ErrorResponseException(HttpStatus.NOT_FOUND, asProblemDetail(errorMessage), null)

private fun asProblemDetail(message: String): ProblemDetail {
    val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message)
    problemDetail.title = "Resource not found"
    problemDetail.type = URI("https://www.rfc-editor.org/rfc/rfc7807")
    problemDetail.setProperty("timestamp", LocalDateTime.now())
    return problemDetail
}