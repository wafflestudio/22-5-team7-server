package com.toyProject7.karrot.profile.controller

import com.toyProject7.karrot.article.controller.Item
import com.toyProject7.karrot.manner.controller.Manner
import com.toyProject7.karrot.profile.service.ProfileService
import com.toyProject7.karrot.review.controller.Review
import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController(
    private val profileService: ProfileService,
) {
    @GetMapping("/api/mypage")
    fun mypage(
        @AuthUser user: User,
    ): ResponseEntity<MyPageResponse> {
        return ResponseEntity.ok(MyPageResponse(user.nickname, user.temperature, user.imagePresignedUrl))
    }

    @GetMapping("/api/mypage/profile")
    fun getMyProfile(
        @AuthUser user: User,
    ): ResponseEntity<ProfileResponse> {
        val profile = profileService.getMyProfile(user)
        return ResponseEntity.ok(profile)
    }

    @GetMapping("/api/profile")
    fun getProfile(
        @RequestParam nickname: String,
    ): ResponseEntity<ProfileResponse> {
        val profile = profileService.getProfile(nickname)
        return ResponseEntity.ok(profile)
    }
    // GET /api/profile?nickname=hello%2Fworld

    @GetMapping("/api/profile/sells")
    fun getProfileSells(
        @RequestParam nickname: String,
        @RequestParam articleId: Long,
    ): ResponseEntity<List<Item>> {
        val itemList: List<Item> = profileService.getProfileSells(nickname, articleId)
        return ResponseEntity.ok(itemList)
    }
    // GET /api/profile/sells?nickname=hello%2Fworld&articleId=123

    @PutMapping("/api/mypage/profile/edit")
    fun editProfile(
        @AuthUser user: User,
        @RequestBody request: EditProfileRequest,
    ): ResponseEntity<ProfileResponse> {
        val profile = profileService.editProfile(user, request)
        return ResponseEntity.ok(profile)
    }

    @GetMapping("/api/profile/manners")
    fun getManners(
        @RequestParam nickname: String,
        @AuthUser user: User,
    ): ResponseEntity<MannersResponse> {
        val manners = profileService.getManner(user, nickname)
        return ResponseEntity.ok(manners)
    }
    // GET /api/profile/manners?nickname=hello%2Fworld

    @GetMapping("/api/profile/reviews")
    fun getReviews(
        @RequestParam nickname: String,
        @RequestParam reviewId: Long,
    ): ResponseEntity<ReviewsResponse> {
        val reviews = profileService.getPreviousReviews(nickname, reviewId)
        return ResponseEntity.ok(reviews)
    }
    // GET /api/profile/reviews?nickname=hello%2Fworld&reviewId=123
}

typealias ProfileResponse = Profile

typealias MannersResponse = List<Manner>

typealias ReviewsResponse = List<Review>

data class MyPageResponse(
    val nickname: String,
    val temperature: Double,
    val imagePresignedUrl: String,
)

data class EditProfileRequest(
    val nickname: String,
    val location: String,
    val imageCount: Int,
)
