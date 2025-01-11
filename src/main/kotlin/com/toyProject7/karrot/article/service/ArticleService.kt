package com.toyProject7.karrot.article.service

import com.toyProject7.karrot.article.ArticleNotFoundException
import com.toyProject7.karrot.article.ArticlePermissionDeniedException
import com.toyProject7.karrot.article.PresignedUrlListIsEmptyException
import com.toyProject7.karrot.article.S3UrlListIsEmptyException
import com.toyProject7.karrot.article.controller.Article
import com.toyProject7.karrot.article.controller.PostArticleRequest
import com.toyProject7.karrot.article.persistence.ArticleEntity
import com.toyProject7.karrot.article.persistence.ArticleLikesEntity
import com.toyProject7.karrot.article.persistence.ArticleLikesRepository
import com.toyProject7.karrot.article.persistence.ArticleRepository
import com.toyProject7.karrot.image.service.ImageService
import com.toyProject7.karrot.user.service.UserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val articleLikesRepository: ArticleLikesRepository,
    private val userService: UserService,
    private val imageService: ImageService,
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
                imageS3Url = emptyList(),
                imagePresignedUrl = emptyList(),
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
                viewCount = 1,
            )
        if (request.imageCount > 0) {
            val imageS3Url: List<String> = imageService.postImageUrl("article", articleEntity.id!!, request.imageCount)
            articleEntity.imageS3Url = imageS3Url
            if (imageS3Url.isEmpty()) throw S3UrlListIsEmptyException()
            val imagePresignedUrl: List<String> = imageService.generatePresignedUrl(imageS3Url)
            articleEntity.imagePresignedUrl = imagePresignedUrl
            if (imagePresignedUrl.isEmpty()) throw PresignedUrlListIsEmptyException()
            articleEntity.updatedAt = Instant.now()
        }
        articleRepository.save(articleEntity)
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
        articleEntity.title = request.title
        articleEntity.content = request.content
        articleEntity.price = request.price
        articleEntity.location = request.location
        if (articleEntity.imageS3Url.isNotEmpty()) {
            imageService.deleteImageUrl(articleEntity.imageS3Url)
        }
        if (request.imageCount > 0) {
            val imageS3Url: List<String> = imageService.postImageUrl("article", articleId, request.imageCount)
            articleEntity.imageS3Url = imageS3Url
            val imagePresignedUrl: List<String> = imageService.generatePresignedUrl(imageS3Url)
            articleEntity.imagePresignedUrl = imagePresignedUrl
            articleEntity.updatedAt = Instant.now()
        }
        articleEntity.viewCount += 1
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
        if (articleEntity.imageS3Url.isNotEmpty()) {
            imageService.deleteImageUrl(articleEntity.imageS3Url)
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
        val article: List<ArticleEntity> = listOf(articleEntity)
        refreshPresignedUrlIfExpired(article)
        articleEntity.viewCount += 1
        articleRepository.save(articleEntity)
        return Article.fromEntity(articleEntity)
    }

    @Transactional
    fun refreshPresignedUrlIfExpired(articles: List<ArticleEntity>) {
        articles.forEach { article ->
            if (article.imageS3Url.isNotEmpty() && ChronoUnit.MINUTES.between(article.updatedAt, Instant.now()) >= 10) {
                val presigned = imageService.generatePresignedUrl(article.imageS3Url)
                if (presigned.isEmpty()) throw PresignedUrlListIsEmptyException()
                article.imagePresignedUrl = presigned
                article.updatedAt = Instant.now()
                articleRepository.save(article)
            }
        }
    }

    @Transactional
    fun getPreviousArticles(articleId: Long): List<ArticleEntity> {
        val articles = articleRepository.findTop10ByIdBeforeOrderByCreatedAtDesc(articleId)
        refreshPresignedUrlIfExpired(articles)
        return articles
    }

    @Transactional
    fun getArticlesThatUserLikes(
        id: String,
        articleId: Long,
    ): List<ArticleEntity> {
        val userEntity = userService.getUserEntityById(id)
        val articles =
            articleLikesRepository.findTop10ByUserAndArticleIdLessThanOrderByArticleIdDesc(
                userEntity,
                articleId,
            ).map { it.article }
        refreshPresignedUrlIfExpired(articles)
        return articles
    }

    @Transactional
    fun getArticlesBySeller(
        id: String,
        articleId: Long,
    ): List<ArticleEntity> {
        val seller = userService.getUserEntityById(id)
        val articles = articleRepository.findTop10BySellerAndIdLessThanOrderByIdDesc(seller, articleId)
        refreshPresignedUrlIfExpired(articles)
        return articles
    }

    @Transactional
    fun getArticlesByBuyer(
        id: String,
        articleId: Long,
    ): List<ArticleEntity> {
        val buyer = userService.getUserEntityById(id)
        val articles = articleRepository.findTop10ByBuyerAndIdLessThanOrderByIdDesc(buyer, articleId)
        refreshPresignedUrlIfExpired(articles)
        return articles
    }
}
