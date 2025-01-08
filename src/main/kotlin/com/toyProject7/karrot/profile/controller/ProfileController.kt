package com.toyProject7.karrot.profile.controller

import com.toyProject7.karrot.manner.controller.Manner
import com.toyProject7.karrot.profile.service.ProfileService
import com.toyProject7.karrot.review.controller.Review
import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController(
    private val profileService: ProfileService,
) {
    @GetMapping("/api/mypage/profile")
    fun getMyProfile(
        @AuthUser user: User,
    ): ResponseEntity<ProfileResponse> {
        val profile = profileService.getMyProfile(user)
        return ResponseEntity.ok(profile)
    }

    @GetMapping("/api/profile/{nickname}")
    fun getProfile(
        @PathVariable nickname: String,
    ): ResponseEntity<ProfileResponse> {
        val profile = profileService.getProfile(nickname)
        return ResponseEntity.ok(profile)
    }

    @GetMapping("/api/profile/{nickname}/manners")
    fun getManners(
        @PathVariable nickname: String,
    ): ResponseEntity<MannersResponse> {
        val manners = profileService.getManner(nickname)
        return ResponseEntity.ok(manners)
    }

    @GetMapping("/api/profile/{nickname}/reviews")
    fun getReviews(
        @PathVariable nickname: String,
        @RequestParam("reviewId") reviewId: Long,
    ): ResponseEntity<ReviewsResponse> {
        val reviews = profileService.getPreviousReviews(nickname, reviewId)
        return ResponseEntity.ok(reviews)
    }
}

typealias ProfileResponse = Profile

typealias MannersResponse = List<Manner>

typealias ReviewsResponse = List<Review>
