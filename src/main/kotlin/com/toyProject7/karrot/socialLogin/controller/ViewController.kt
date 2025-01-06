package com.toyProject7.karrot.socialLogin.controller

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ViewController {

    @GetMapping("/profile")
    fun profile(token: OAuth2AuthenticationToken, model: Model): String {
        model.addAttribute("name", w)
        model.addAttribute("email", token.principal.getAttribute<String>("email"))
        model.addAttribute("photo", token.principal.getAttribute<String>("picture"))
        return "user-profile"
    }

    @GetMapping("/login")
    fun login(): String {
        return "custom_login"
    }
}