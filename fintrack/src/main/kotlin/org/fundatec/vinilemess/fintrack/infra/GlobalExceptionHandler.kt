package org.fundatec.vinilemess.fintrack.infra

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.URI

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(
        authenticationException: AuthenticationException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemDetail> {
        logger.error("Authentication Error", authenticationException)
        val unauthorizedMessage = authenticationException.message ?: "Unauthorized, please make sure you are authenticated correctly"
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, unauthorizedMessage)
        problemDetail.instance = URI.create(request.requestURI)
        problemDetail.title = "Authentication Error"

        return ResponseEntity.of(problemDetail)
            .build()
    }

    @ExceptionHandler(Exception::class)
    fun handleAuthenticationException(
        exception: Exception?,
        request: HttpServletRequest
    ): ResponseEntity<ProblemDetail> {
        logger.error("Unknown error", exception)
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Error :(")
        problemDetail.instance = URI.create(request.requestURI)
        problemDetail.title = "Internal Server Error"

        return ResponseEntity.of(problemDetail)
            .build()
    }
}