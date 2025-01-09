package com.toyProject7.karrot.user.controller

import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {
    @PostMapping("/auth/sign/up")
    fun signUp(
        @RequestBody request: SignUpRequest,
    ): ResponseEntity<SignUpResponse> {
        val user = userService.signUp(request.nickname, request.userId, request.password, request.email)
        return ResponseEntity.ok(SignUpResponse(user))
    }

    @PostMapping("/auth/sign/in")
    fun signIn(
        @RequestBody request: SignInRequest,
    ): ResponseEntity<SignInResponse> {
        val (user, accessToken) = userService.signIn(request.userId, request.password)
        return ResponseEntity.ok(SignInResponse(user, accessToken))
    }

    @GetMapping("/auth/me")
    fun me(
        @AuthUser user: User,
    ): ResponseEntity<UserMeResponse> {
        if (user.userId == null) {
            throw IllegalStateException("User ID cannot be null for NormalUser")
        }
        return ResponseEntity.ok(UserMeResponse(user.id, user.nickname))
    }
}

data class SignUpRequest(
    val nickname: String,
    val userId: String,
    val password: String,
    val email: String,
)

data class SignUpResponse(
    val user: User,
)

data class SignInRequest(
    val userId: String,
    val password: String,
)

data class SignInResponse(
    val user: User,
    val accessToken: String,
)

data class UserMeResponse(
    val id: String,
    val nickname: String,
)
