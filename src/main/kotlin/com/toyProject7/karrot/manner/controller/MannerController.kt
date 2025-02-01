package com.toyProject7.karrot.manner.controller

import com.toyProject7.karrot.manner.service.MannerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MannerController(
    private val mannerService: MannerService,
) {
    @PutMapping("/api/profile/praise/{mannerType}")
    fun increaseMannerCount(
        @RequestParam nickname: String,
        @PathVariable mannerType: MannerType,
    ): ResponseEntity<String> {
        mannerService.increaseMannerCount(nickname, mannerType)
        return ResponseEntity.noContent().build()
    }
    // PUT /api/profile/praise/{mannerType}?nickname=hello%2Fworld
}
