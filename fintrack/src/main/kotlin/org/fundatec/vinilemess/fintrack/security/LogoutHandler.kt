package org.fundatec.vinilemess.fintrack.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.fundatec.vinilemess.fintrack.security.repository.TokenRepository
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component

@Component
class LogoutHandler(private val tokenRepository: TokenRepository):  LogoutHandler {

    override fun logout(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        request.getHeader(HttpHeaders.AUTHORIZATION)?.let {
            if (it.startsWith("Bearer ")) {
                val token = it.substring(7)

                tokenRepository.findToken(token)?.let { storedToken ->
                    val invalidatedToken = storedToken.invalidateToken()
                    tokenRepository.upsert(invalidatedToken)
                    SecurityContextHolder.clearContext()
                }
            }
        }
    }
}