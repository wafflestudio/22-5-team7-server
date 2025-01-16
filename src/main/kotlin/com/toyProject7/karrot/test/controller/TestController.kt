package com.toyProject7.karrot.test.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/api/test")
    fun getTest(): ResponseEntity<TestResponse> {
        return ResponseEntity.ok(TestResponse(1, 2, "2번 테스트 제목", "2번 테스트 본문내용"))
    }

    @GetMapping("/api/test2")
    fun getTest2(): ResponseEntity<TestResponse> {
        return ResponseEntity.ok(TestResponse(11, 22, "22번 테스트 제목", "22번 테스트 본문내용"))
    }

    @PostMapping("/api/test3")
    fun postTest(
        @RequestBody request: TestResponse,
    ): ResponseEntity<TestResponse> {
        val response = TestResponse(request.userId, request.id, request.title, request.body)
        return ResponseEntity.ok(response)
    }
}

data class TestResponse(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
)
