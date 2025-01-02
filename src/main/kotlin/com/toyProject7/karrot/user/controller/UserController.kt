package com.toyProject7.karrot.user.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class UserController {
    @RequestMapping("/")
    fun home(): String {
        return "Welcome!"
    }

    @RequestMapping("/user")
    fun user(principal: Principal): Principal {
        return principal
    }
}
