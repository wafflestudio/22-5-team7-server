package com.toyProject7.karrot.user.controller

import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController (
    private val userService: UserService,
) {
    @PostMapping("/auth/register")
    fun signUp(
        @RequestBody request: SignUpRequest,
    ): ResponseEntity<SignUpResponse> {
        val user = userService.signUp(request.nickname, request.userId, request.password, request.email)
        return ResponseEntity.ok(SignUpResponse(user))
    }

    @PostMapping("/auth/login")
    fun signIn(
        @RequestBody request: SignInRequest,
    ): ResponseEntity<SignInResponse> {
        val (_, accessToken) = userService.signIn(request.userId, request.password)
        return ResponseEntity.ok(SignInResponse(accessToken))
    }

    @GetMapping("/users/me")
    fun me(
        @AuthUser user: User,
    ): ResponseEntity<UserMeResponse> {
        return ResponseEntity.ok(UserMeResponse(user.userId, user.nickname))
    }

}

data class SignUpRequest(
    val nickname: String,
    val userId: String,
    val password: String,
    val email: String,
)

data class SignUpResponse(
    val user: User
)

data class SignInRequest(
    val userId: String,
    val password: String,
)

data class SignInResponse(
    val accessToken: String,
)

data class UserMeResponse(
    val userId: String,
    val nickname: String,
)