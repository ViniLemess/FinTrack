package org.fundatec.vinilemess.tcc.fintrack.infra.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException
import java.net.URI
import java.time.LocalDateTime

class EmailTakenException(errorMessage: String):
    ErrorResponseException(HttpStatus.CONFLICT, asProblemDetail(errorMessage), null)

private fun asProblemDetail(message: String): ProblemDetail {
    val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, message)
    problemDetail.title = "Resource already exists"
    problemDetail.type = URI("https://www.rfc-editor.org/rfc/rfc7807")
    problemDetail.setProperty("timestamp", LocalDateTime.now())
    return problemDetail
}