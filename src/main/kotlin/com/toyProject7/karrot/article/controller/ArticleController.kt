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
    ): ResponseEntity<LikedArticlesResponse> {
        val articles = articleService.listLikedArticles(user.userId)
        return ResponseEntity.ok(articles)
    }

    @GetMapping("/api/mypage/sells")
    fun getSellingAndSoldArticles(
        @AuthUser user: User,
    ): ResponseEntity<SellingAndSoldArticlesResponse> {
        val articles = articleService.listSellingAndSoldArticles(user.userId)
        return ResponseEntity.ok(articles)
    }

    @GetMapping("/api/mypage/buys")
    fun getBoughtArticles(
        @AuthUser user: User,
    ): ResponseEntity<BoughtArticlesResponse> {
        val articles = articleService.listBoughtArticles(user.userId)
        return ResponseEntity.ok(articles)
    }
}

typealias LikedArticlesResponse = List<Article>

data class SellingAndSoldArticlesResponse(
    val sellingArticles: List<Article>,
    val soldArticles: List<Article>,
)

typealias BoughtArticlesResponse = List<Article>
