package com.toyProject7.karrot.user.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
    @RequestMapping("/")
    fun home(): String {
        return "Welcome!"
    }

    @RequestMapping("/user")
    fun user(
        @AuthenticationPrincipal oAuth2User: OAuth2User,
    ): Map<String, Any?> {
        // Extracting attributes
        val attributes = oAuth2User.attributes

        // Extract username and email
        val username = attributes["name"] as String? // You can customize the key to get the desired username
        val email = attributes["email"] as String?

        return mapOf(
            "username" to username,
            "email" to email,
        )
    }
}
