package com.toyProject7.karrot.article.controller

import com.toyProject7.karrot.article.persistence.ArticleEntity
import com.toyProject7.karrot.article.service.ArticleService
import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ArticleController(
    private val articleService: ArticleService,
) {
    @PostMapping("/item/post")
    fun postArticle(
        @RequestBody request: PostArticleRequest,
        @AuthUser user: User,
    ): ResponseEntity<Article> {
        val article = articleService.postArticle(request, user.id)
        return ResponseEntity.ok(article)
    }

    @PutMapping("/item/edit/{articleId}")
    fun editArticle(
        @RequestBody request: PostArticleRequest,
        @PathVariable articleId: Long,
        @AuthUser user: User,
    ): ResponseEntity<Article> {
        val article = articleService.editArticle(articleId, request, user.id)
        return ResponseEntity.ok(article)
    }

    @DeleteMapping("/item/delete/{articleId}")
    fun deleteArticle(
        @PathVariable articleId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        articleService.deleteArticle(articleId, user.id)
        return ResponseEntity.ok("Deleted Successfully")
    }

    @PostMapping("/item/like/{articleId}")
    fun likeArticle(
        @PathVariable articleId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        articleService.likeArticle(articleId, user.id)
        return ResponseEntity.ok("Liked Successfully")
    }

    @DeleteMapping("/item/unlike/{articleId}")
    fun unlikeArticle(
        @PathVariable articleId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        articleService.unlikeArticle(articleId, user.id)
        return ResponseEntity.ok("Unliked Successfully")
    }

    @GetMapping("/item/get/{articleId}")
    fun getArticle(
        @PathVariable articleId: Long,
        @AuthUser user: User,
    ): ResponseEntity<Article> {
        val article = articleService.getArticle(articleId, user.id)
        return ResponseEntity.ok(article)
    }

    @GetMapping("/home")
    fun getPreviousArticles(
        @RequestParam("articleId") articleId: Long,
    ): ResponseEntity<List<Item>> {
        val articles: List<ArticleEntity> = articleService.getPreviousArticles(articleId)
        val response: List<Item> =
            articles.map { article ->
                Item.fromArticle(Article.fromEntity(article))
            }
        return ResponseEntity.ok(response)
    }

    @GetMapping("/mypage/likes")
    fun getArticlesThatUserLikes(
        @RequestParam("articleId") articleId: Long,
        @AuthUser user: User,
    ): ResponseEntity<List<Item>> {
        val articles: List<ArticleEntity> = articleService.getArticlesThatUserLikes(user.id, articleId)
        val response =
            articles.map { article ->
                Item.fromArticle(Article.fromEntity(article))
            }
        return ResponseEntity.ok(response)
    }

    @GetMapping("/mypage/sells")
    fun getArticlesBySeller(
        @RequestParam("articleId") articleId: Long,
        @AuthUser user: User,
    ): ResponseEntity<List<Item>> {
        val articles: List<ArticleEntity> = articleService.getArticlesBySeller(user.id, articleId)
        val response: List<Item> =
            articles.map { article ->
                Item.fromArticle(Article.fromEntity(article))
            }
        return ResponseEntity.ok(response)
    }

    @GetMapping("/mypage/buys")
    fun getArticlesByBuyer(
        @RequestParam("articleId") articleId: Long,
        @AuthUser user: User,
    ): ResponseEntity<List<Item>> {
        val articles = articleService.getArticlesByBuyer(user.id, articleId)
        val response: List<Item> =
            articles.map { article ->
                Item.fromArticle(Article.fromEntity(article))
            }
        return ResponseEntity.ok(response)
    }
}

data class PostArticleRequest(
    val title: String,
    val content: String,
    val price: Int,
    val location: String,
    val imageCount: Int,
)
