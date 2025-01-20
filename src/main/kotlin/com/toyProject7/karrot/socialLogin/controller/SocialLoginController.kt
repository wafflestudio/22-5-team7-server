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
}
