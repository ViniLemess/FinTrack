package org.fundatec.vinilemess.fintrack.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.fundatec.vinilemess.fintrack.security.repository.TokenRepository
import org.fundatec.vinilemess.fintrack.security.service.JwtService
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
        private val jwtService: JwtService,
        private val tokenRepository: TokenRepository,
        private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    private fun doesNotHaveValidAuthorizationHeader(request: HttpServletRequest): Boolean {
        val authorizationHeader = request.getHeader("Authorization")
        return authorizationHeader.isNullOrEmpty() && !hasValidAuthorizationHeader(request)
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (isLoginRequest(request) || doesNotHaveValidAuthorizationHeader(request)) {
            filterChain.doFilter(request, response)
            return
        }

        val token: String = extractJwtTokenFromAuthorizationHeader(request)
        val username: String = jwtService.extractUsername(token)

        SecurityContextHolder.getContext().authentication?.let {
            filterChain.doFilter(request, response)
            return
        }

        authenticateUser(request, token, username)
    }

    private fun authenticateUser(request: HttpServletRequest, jwtToken: String, username: String) {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
        val token: Token? = tokenRepository.findToken(jwtToken)

        if (jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid(token)) {
            val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
            )
            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authToken
        }
    }

    private fun isTokenValid(token: Token?) = token?.let {
        !it.isExpired && !it.isRevoked
    } ?: false

    private fun isLoginRequest(request: HttpServletRequest): Boolean {
        return request.method == HttpMethod.POST.name() && request.servletPath == "/login"
    }

    private fun hasValidAuthorizationHeader(request: HttpServletRequest): Boolean {
        return request.getHeader("Authorization")?.startsWith("Bearer") ?: false
    }

    private fun extractJwtTokenFromAuthorizationHeader(request: HttpServletRequest): String {
        return request.getHeader("Authorization").substring(7)
    }
}
