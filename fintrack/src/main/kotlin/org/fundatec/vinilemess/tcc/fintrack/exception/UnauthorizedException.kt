package org.fundatec.vinilemess.tcc.fintrack.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException
import java.net.URI
import java.time.LocalDateTime

class UnauthorizedException(errorMessage: String):
    ErrorResponseException(HttpStatus.UNAUTHORIZED, asProblemDetail(errorMessage), null)

private fun asProblemDetail(message: String): ProblemDetail {
    val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, message)
    problemDetail.title = "Could not authenticate request"
    problemDetail.type = URI("https://www.rfc-editor.org/rfc/rfc7807")
    problemDetail.setProperty("timestamp", LocalDateTime.now())
    return problemDetail
}