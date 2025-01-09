package com.toyProject7.karrot.article.service

import com.toyProject7.karrot.article.ArticleNotFoundException
import com.toyProject7.karrot.article.ArticlePermissionDeniedException
import com.toyProject7.karrot.article.controller.Article
import com.toyProject7.karrot.article.controller.PostArticleRequest
import com.toyProject7.karrot.article.persistence.ArticleEntity
import com.toyProject7.karrot.article.persistence.ArticleLikesEntity
import com.toyProject7.karrot.article.persistence.ArticleLikesRepository
import com.toyProject7.karrot.article.persistence.ArticleRepository
import com.toyProject7.karrot.user.service.UserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val articleLikesRepository: ArticleLikesRepository,
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
        if (articleEntity.seller.userId != user.userId) {
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
        if (articleEntity.seller.userId != user.userId) {
            throw ArticlePermissionDeniedException()
        }
        articleRepository.delete(articleEntity)
    }

    @Transactional
    fun likeArticle(
        articleId: Long,
        id: String,
    ) {
        val articleEntity = articleRepository.findByIdOrNull(articleId) ?: throw ArticleNotFoundException()
        val userEntity = userService.getUserEntityById(id)
        if (articleEntity.articleLikes.any { it.user.id == userEntity.id }) {
            return
        }
        val articleLikesEntity =
            articleLikesRepository.save(
                ArticleLikesEntity(article = articleEntity, user = userEntity, createdAt = Instant.now(), updatedAt = Instant.now()),
            )
        articleEntity.articleLikes += articleLikesEntity
        articleRepository.save(articleEntity)
    }

    @Transactional
    fun unlikeArticle(
        articleId: Long,
        id: String,
    ) {
        val articleEntity = articleRepository.findByIdOrNull(articleId) ?: throw ArticleNotFoundException()
        val userEntity = userService.getUserEntityById(id)
        val toBeRemoved: ArticleLikesEntity = articleEntity.articleLikes.find { it.user.id == userEntity.id } ?: return
        articleEntity.articleLikes -= toBeRemoved
        articleLikesRepository.delete(toBeRemoved)
        articleRepository.save(articleEntity)
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
    fun getArticlesThatUserLikes(
        id: String,
        articleId: Long,
    ): List<ArticleEntity> {
        val userEntity = userService.getUserEntityById(id)
        return articleLikesRepository.findTop10ByUserAndArticleIdLessThanOrderByArticleIdDesc(
            userEntity,
            articleId,
        ).map { it.article }
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
