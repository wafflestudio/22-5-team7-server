package com.toyProject7.karrot.socialLogin.service

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class MainController {

    @RequestMapping("/")
    fun home(): String {
        return "Home"
    }

    @RequestMapping("/user")
    fun user(principal: Principal): Principal {
        return principal
    }
}