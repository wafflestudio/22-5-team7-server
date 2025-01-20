package com.toyProject7.karrot.socialLogin.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SocialLoginController {
    // Endpoint to initiate OAuth2 login for a specific provider
    @GetMapping("/api/social/login/{provider}")
    fun socialLogin(
        @PathVariable provider: String,
    ): ResponseEntity<Map<String, String>> {
        val redirectUrl = "/oauth2/authorization/$provider"
        val responseBody = mapOf("redirectUrl" to redirectUrl)
        return ResponseEntity.ok(responseBody)
    }

    @GetMapping("/api/social/me")
    fun getCurrentUser(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
    ): ResponseEntity<Any> {
        if (userPrincipal == null) {
            return ResponseEntity.status(401).body("Unauthorized")
        }

        // Build a response with user details
        val response =
            mapOf(
                "id" to userPrincipal.id,
                "email" to userPrincipal.username,
                "name" to userPrincipal.getNickname(),
            )

        return ResponseEntity.ok(response)
    }

    private val logger: Logger = LoggerFactory.getLogger(SocialLoginController::class.java)

    @GetMapping("/oauth2/authorization/google")
    fun getOAuth2Authorization(clientRegistration: ClientRegistration): String {
        // Manually construct the redirect URI if necessary
        val redirectUri = clientRegistration.redirectUri
        logger.info("Redirect URI: {}", redirectUri)
        logger.info("Google Client ID: {}", clientRegistration.clientId)
        logger.info("Google Client SECRET: {}", clientRegistration.clientSecret)
        // Do not log client secret in a real-world scenario
        // logger.info("Client Secret: {}", clientRegistration.clientSecret)

        return "Check logs for OAuth details"
    }
}
