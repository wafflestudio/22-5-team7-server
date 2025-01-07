package com.toyProject7.karrot.article.controller

import com.toyProject7.karrot.article.service.ArticleService
import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleController(
    private val articleService: ArticleService,
) {
    @GetMapping("/api/mypage/likes")
    fun getLikedArticles(
        @AuthUser user: User,
    ): ResponseEntity<LikedItemsResponse> {
        val articles = articleService.listLikedArticles(user.userId)
        return ResponseEntity.ok(articles)
    }

    @GetMapping("/api/mypage/sells")
    fun getSellingAndSoldArticles(
        @AuthUser user: User,
    ): ResponseEntity<SellingAndSoldItemsResponse> {
        val articles = articleService.listSellingAndSoldArticles(user.userId)
        return ResponseEntity.ok(articles)
    }

    @GetMapping("/api/mypage/buys")
    fun getBoughtArticles(
        @AuthUser user: User,
    ): ResponseEntity<BoughtItemsResponse> {
        val articles = articleService.listBoughtArticles(user.userId)
        return ResponseEntity.ok(articles)
    }
}

typealias LikedItemsResponse = List<Item>

data class SellingAndSoldItemsResponse(
    val sellingItems: List<Item>,
    val soldItems: List<Item>,
)

typealias BoughtItemsResponse = List<Item>
