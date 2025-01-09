package com.toyProject7.karrot.article.service

import com.toyProject7.karrot.article.ArticleNotFoundException
import com.toyProject7.karrot.article.ArticlePermissionDeniedException
import com.toyProject7.karrot.article.controller.Article
import com.toyProject7.karrot.article.controller.PostArticleRequest
import com.toyProject7.karrot.article.persistence.ArticleEntity
import com.toyProject7.karrot.article.persistence.ArticleRepository
import com.toyProject7.karrot.user.service.UserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userService: UserService,
) {
    @Transactional
    fun postArticle(
        request: PostArticleRequest,
        id: String,
    ): Article {
        val user = userService.getUserEntityById(id)
        val articleEntity =
            ArticleEntity(
                seller = user,
                buyer = null,
                title = request.title,
                content = request.content,
                price = request.price,
                status = "판매 중",
                location = request.location,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            ).let {
                articleRepository.save(it)
            }
        return Article.fromEntity(articleEntity)
    }

    @Transactional
    fun editArticle(
        articleId: Long,
        request: PostArticleRequest,
        id: String,
    ): Article {
        val user = userService.getUserEntityById(id)
        val articleEntity = articleRepository.findByIdOrNull(articleId) ?: throw ArticleNotFoundException()
        if (articleEntity.seller.id != user.id) {
            throw ArticlePermissionDeniedException()
        }
        request.title.let { articleEntity.title = it }
        request.content.let { articleEntity.content = it }
        request.price.let { articleEntity.price = it }
        request.location.let { articleEntity.location = it }
        articleEntity.updatedAt = Instant.now()
        articleRepository.save(articleEntity)
        return Article.fromEntity(articleEntity)
    }

    @Transactional
    fun deleteArticle(
        articleId: Long,
        id: String,
    ) {
        val user = userService.getUserEntityById(id)
        val articleEntity = articleRepository.findByIdOrNull(articleId) ?: throw ArticleNotFoundException()
        if (articleEntity.seller.id != user.id) {
            throw ArticlePermissionDeniedException()
        }
        articleRepository.delete(articleEntity)
    }

    @Transactional
    fun getArticle(articleId: Long): Article {
        val articleEntity = articleRepository.findByIdOrNull(articleId) ?: throw ArticleNotFoundException()
        return Article.fromEntity(articleEntity)
    }

    @Transactional
    fun getPreviousArticles(articleId: Long): List<ArticleEntity> {
        return articleRepository.findTop10ByIdBeforeOrderByCreatedAtDesc(articleId)
    }

    @Transactional
    fun getArticlesBySeller(
        id: String,
        articleId: Long,
    ): List<ArticleEntity> {
        val seller = userService.getUserEntityById(id)
        return articleRepository.findTop10BySellerAndIdLessThanOrderByIdDesc(seller, articleId)
    }

    @Transactional
    fun getArticlesByBuyer(
        id: String,
        articleId: Long,
    ): List<ArticleEntity> {
        val buyer = userService.getUserEntityById(id)
        return articleRepository.findTop10ByBuyerAndIdLessThanOrderByIdDesc(buyer, articleId)
    }
}
