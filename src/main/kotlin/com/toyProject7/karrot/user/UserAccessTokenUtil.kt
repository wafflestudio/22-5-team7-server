package com.toyProject7.karrot.user

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.util.Date

object UserAccessTokenUtil {
    private val SECRET_KEY =
        System.getenv("JWT_SECRET_KEY")
            ?.let { Keys.hmacShaKeyFor(it.toByteArray(StandardCharsets.UTF_8)) }
            ?: throw IllegalStateException("JWT_SECRET_KEY is not set!")

    private const val JWT_EXPIRATION_TIME = 1000 * 60 * 60 * 2 // 2 hours

    fun generateAccessToken(id: String): String {
        val now = Date()
        val expiryDate = Date(now.time + JWT_EXPIRATION_TIME)
        return Jwts.builder()
            .signWith(SECRET_KEY)
            .setSubject(id)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .compact()
    }

    fun validateAccessTokenGetUserId(accessToken: String): String? {
        return try {
            val claims =
                Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(accessToken)
                    .body
            if (claims.expiration.before(Date())) null else claims.subject
        } catch (e: Exception) {
            println("Token validation failed. Please try again.")
            null
        }
    }
}
