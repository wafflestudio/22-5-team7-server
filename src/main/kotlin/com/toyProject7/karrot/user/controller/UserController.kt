package com.toyProject7.karrot.user.controller

import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    // private val userService: UserService,
)

data class SignUpRequest(
    val userId: String,
    val password: String,
    val nickname: String,
)

data class SignUpResponse(
    val user: User,
)
