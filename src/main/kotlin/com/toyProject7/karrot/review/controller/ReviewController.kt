package com.toyProject7.karrot.review.controller

import com.toyProject7.karrot.review.service.ReviewService
import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URLDecoder

@RestController
class ReviewController(
    private val reviewService: ReviewService,
) {
    @PostMapping("/api/{sellerNickname}/review")
    fun createReview(
        @RequestBody request: ReviewCreateRequest,
        @PathVariable sellerNickname: String,
        @AuthUser user: User,
    ): ResponseEntity<ReviewCreateResponse> {
        // Decode the sellerNickname
        val decodedSellerNickname = URLDecoder.decode(sellerNickname, "UTF-8")

        val review = reviewService.createReview(decodedSellerNickname, user.nickname, request.content, request.location)
        return ResponseEntity.status(201).body(review)
    }
}

data class ReviewCreateRequest(
    val content: String,
    val location: String,
)

typealias ReviewCreateResponse = Review
