package org.fundatec.vinilemess.fintrack.infra

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.security.access.AccessDeniedException
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

        return problemDetailForStatusBuilder(HttpStatus.UNAUTHORIZED)
            .withDetail(unauthorizedMessage)
            .withInstance(request.requestURI)
            .withTitle("Authentication Error")
            .build()
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        exception: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ProblemDetail> {
        logger.error("Unknown error", exception)

        return problemDetailForStatusBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
            .withDetail("Unknown Error :(")
            .withInstance(request.requestURI)
            .withTitle("Internal Server Error")
            .build()
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        exception: AccessDeniedException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemDetail> {
        logger.error("Access Denied", exception)

        return problemDetailForStatusBuilder(HttpStatus.FORBIDDEN)
            .withTitle("Forbidden Access")
            .withDetail(exception.message?: "Access Denied")
            .withInstance(request.requestURI)
            .build()
    }

    private fun problemDetailForStatusBuilder(httpStatus: HttpStatus): ProblemDetailsResponseEntityBuilder {
        return ProblemDetailsResponseEntityBuilder(ProblemDetail.forStatus(httpStatus))
    }

    private class ProblemDetailsResponseEntityBuilder(val problemDetail: ProblemDetail) {

        fun withTitle(title: String): ProblemDetailsResponseEntityBuilder {
            problemDetail.title = title
            return this
        }

        fun withDetail(detail: String): ProblemDetailsResponseEntityBuilder {
            problemDetail.detail = detail
            return this
        }

        fun withInstance(uri: String): ProblemDetailsResponseEntityBuilder {
            problemDetail.instance = URI.create(uri)
            return this
        }

        fun build(): ResponseEntity<ProblemDetail> {
            return ResponseEntity.of(this.problemDetail)
                .build()
        }
    }
}