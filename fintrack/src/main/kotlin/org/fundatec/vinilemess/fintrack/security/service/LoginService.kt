package org.fundatec.vinilemess.fintrack.security.service

import org.fundatec.vinilemess.fintrack.exception.UnauthorizedException
import org.fundatec.vinilemess.fintrack.security.Token
import org.fundatec.vinilemess.fintrack.security.contract.request.LoginRequest
import org.fundatec.vinilemess.fintrack.security.contract.response.LoginResponse
import org.fundatec.vinilemess.fintrack.security.repository.TokenRepository
import org.fundatec.vinilemess.fintrack.user.contract.User
import org.fundatec.vinilemess.fintrack.user.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val authenticationManager: AuthenticationManager
) {

    fun authenticateUser(loginRequest: LoginRequest): LoginResponse {
        loginRequest.validateRequest()
        performAuthentication(loginRequest.email, loginRequest.password)

        return userRepository.findByEmail(loginRequest.email)?.let { user ->
            val token: String = jwtService.generateToken(user)
            val refreshToken: String = jwtService.generateRefreshToken(user)
            revokeAllUserTokens(user)
            saveTokenForUser(user, token)
            createAuthorizedOutput(user, token, refreshToken)
        } ?: throw UnauthorizedException("Username or Password incorrect")
    }

    private fun performAuthentication(username: String, password: String) {
        val authRequest =
            UsernamePasswordAuthenticationToken(username, password)
        authenticationManager.authenticate(authRequest)
    }

    private fun revokeAllUserTokens(user: User) {
        val validUserTokens: Collection<Token> = tokenRepository.findValidTokensByUser(user.id!!)
        if (validUserTokens.isNotEmpty()) {
            val revokedTokens = user.tokens.map { it.invalidateToken() }
            tokenRepository.saveAll(revokedTokens)
        }
    }

    private fun saveTokenForUser(savedUser: User, token: String) {
        val createdToken = Token(
            id = null,
            user = savedUser,
            token = token,
            isExpired = false,
            isRevoked = false
        )

        tokenRepository.upsert(createdToken)
    }

    private fun createAuthorizedOutput(user: User, token: String, refreshToken: String): LoginResponse {
        return LoginResponse(
            name = user.name,
            email = user.email,
            token = token,
            refreshToken = refreshToken,
            userSignature = user.transactionSignature,
            role = user.role.name
        )
    }
}