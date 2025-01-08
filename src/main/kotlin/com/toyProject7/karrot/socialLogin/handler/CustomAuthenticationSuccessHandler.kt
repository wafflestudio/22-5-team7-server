package com.toyProject7.karrot.socialLogin.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.toyProject7.karrot.socialLogin.OAuth2AuthenticationException
import com.toyProject7.karrot.user.UserAccessTokenUtil
import com.toyProject7.karrot.user.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationSuccessHandler(
    private val userService: UserService
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication
    ) {
        val oauth2User = authentication.principal as OAuth2User
        val oauth2Token = authentication as OAuth2AuthenticationToken
        val provider = oauth2Token.authorizedClientRegistrationId

        // Extract attributes
        val attributes = oauth2User.attributes
        val providerId = extractProviderId(attributes, provider)
        val email = extractEmail(attributes, provider)
        val name = extractName(attributes, provider)

        // Create or retrieve the user
        val user = userService.createOrRetrieveSocialUser(email, providerId, provider, name)

        // Generate JWT
        val accessToken = UserAccessTokenUtil.generateAccessToken(user.id)

        // Send the JWT in the response body as JSON
        val responseBody = mapOf("accessToken" to accessToken, "user" to user)
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(ObjectMapper().writeValueAsString(responseBody))
    }

    private fun extractProviderId(attributes: Map<String, Any>, provider: String): String {
        return when (provider) {
            "google" -> attributes["sub"] as String
            "naver" -> (attributes["response"] as Map<String, Any>)["id"] as String
            "kakao" -> attributes["id"].toString() // Kakao's id may be Long, convert to String
            else -> throw OAuth2AuthenticationException()
        }
    }

    private fun extractEmail(attributes: Map<String, Any>, provider: String): String {
        return when (provider) {
            "google" -> attributes["email"] as String
            "naver" -> (attributes["response"] as Map<String, Any>)["email"] as String
            "kakao" -> {
                val kakaoAccount = attributes["kakao_account"] as Map<String, Any>
                kakaoAccount["email"] as String
            }
            else -> throw OAuth2AuthenticationException()
        }
    }

    private fun extractName(attributes: Map<String, Any>, provider: String): String {
        return when (provider) {
            "google" -> attributes["name"] as String
            "naver" -> (attributes["response"] as Map<String, Any>)["name"] as String
            "kakao" -> {
                val kakaoAccount = attributes["kakao_account"] as Map<String, Any>
                val profile = kakaoAccount["profile"] as Map<String, Any>
                profile["nickname"] as String
            }
            else -> "Unknown"
        }
    }
}