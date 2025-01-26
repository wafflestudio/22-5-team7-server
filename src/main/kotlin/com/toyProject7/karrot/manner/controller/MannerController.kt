package com.toyProject7.karrot.manner.controller

import com.toyProject7.karrot.manner.service.MannerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URLDecoder

@RestController
class MannerController(
    private val mannerService: MannerService,
) {
    @PutMapping("/api/profile/{nickname}/praise/{mannerType}")
    fun increaseMannerCount(
        @PathVariable nickname: String,
        @PathVariable mannerType: MannerType,
    ): ResponseEntity<String> {
        // Decode the nickname
        val decodedNickname = URLDecoder.decode(nickname, "UTF-8")

        mannerService.increaseMannerCount(decodedNickname, mannerType)
        return ResponseEntity.noContent().build()
    }
}
