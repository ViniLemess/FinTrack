package org.fundatec.vinilemess.fintrack.security.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.SIG.HS256
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${application.security.jwt.secret-key}") val secretKey: String,
    @Value("\${application.security.jwt.token.expiration}") val tokenExpiration: Long,
    @Value("\${application.security.jwt.refresh-token.expiration}") val refreshTokenExpiration: Long
) {

    fun generateToken(userDetails: UserDetails): String {
        val extraClaims = mapOf(
            Pair("role", userDetails.authorities),
            Pair("email", userDetails.username)
        )
        return generateToken(extraClaims, userDetails)
    }

    fun extractUsername(token: String): String {
        return extractClaim<String>(token, Claims::getSubject)
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    fun generateRefreshToken(userDetails: UserDetails): String {
        return buildToken(mapOf<String, Any>(), userDetails, refreshTokenExpiration)
    }

    private fun generateToken(extraClaims: Map<String, *>, userDetails: UserDetails): String {
        return buildToken(extraClaims, userDetails, tokenExpiration)
    }

    private fun buildToken(extraClaims: Map<String, *>, userDetails: UserDetails, expiration: Long): String {
        return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), HS256)
            .compact()
    }

    private fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts
            .parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private fun getSignInKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractClaim(token) {
            it.expiration
        }.before(Date())
    }
}