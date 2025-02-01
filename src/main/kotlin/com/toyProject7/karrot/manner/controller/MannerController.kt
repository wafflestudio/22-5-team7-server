package com.toyProject7.karrot.manner.controller

import com.toyProject7.karrot.manner.service.MannerService
import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MannerController(
    private val mannerService: MannerService,
) {
    @PutMapping("/api/profile/praise")
    fun increaseMannerCount(
        @RequestBody request: MannerRequest,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        mannerService.increaseMannerCount(user, request.nickname, request.mannerType, request.articleId)
        return ResponseEntity.noContent().build()
    }
}

data class MannerRequest(
    val nickname: String,
    val mannerType: MannerType,
    val articleId: Long,
)
