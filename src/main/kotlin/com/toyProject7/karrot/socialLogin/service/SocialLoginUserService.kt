package com.toyProject7.karrot.socialLogin.service

import com.toyProject7.karrot.user.service.UserService
import com.toyProject7.karrot.socialLogin.OAuth2AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class SocialLoginUserService(private val userService: UserService) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private val oAuth2UserService = DefaultOAuth2UserService()

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        // Load the user details from the OAuth2 provider
        val oauth2User = oAuth2UserService.loadUser(userRequest)

        // Extract attributes
        val provider = userRequest.clientRegistration.registrationId
        val email = oauth2User.getAttribute<String>("email") ?: throw OAuth2AuthenticationException()
        val providerId = oauth2User.getAttribute<String>("sub") ?: throw OAuth2AuthenticationException()
        val name = oauth2User.getAttribute<String>("name") ?: throw OAuth2AuthenticationException()

        // Create or retrieve the user
        userService.createOrRetrieveSocialUser(email, providerId, provider, name)
        return oauth2User
    }
}