package com.toyProject7.karrot.socialLogin.service

import com.toyProject7.karrot.user.persistence.UserPrincipal
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class SocialLoginController {

    // Endpoint to initiate OAuth2 login for a specific provider
    @GetMapping("/api/social/login/{provider}")
    fun socialLogin(@PathVariable provider: String): ResponseEntity<Map<String, String>> {
        val redirectUrl = "/oauth2/authorization/$provider"
        val responseBody = mapOf("redirectUrl" to redirectUrl)
        return ResponseEntity.ok(responseBody)
    }

    @GetMapping("/api/social/me")
    fun getCurrentUser(@AuthenticationPrincipal userPrincipal: UserPrincipal?): ResponseEntity<Any> {
        if (userPrincipal == null) {
            return ResponseEntity.status(401).body("Unauthorized")
        }

        // Build a response with user details
        val response = mapOf(
            "id" to userPrincipal.id,
            "email" to userPrincipal.username,
            "name" to userPrincipal.getNickname()
        )

        return ResponseEntity.ok(response)
    }
}