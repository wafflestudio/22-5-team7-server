package com.toyProject7.karrot.user.controller

import com.toyProject7.karrot.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController (
    //private val userService: UserService,
) {

}

data class SignUpRequest(
    val userId: String,
    val password: String,
    val nickname: String,
)

data class SignUpResponse(
    val user: User
)
