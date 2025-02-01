package com.toyProject7.karrot.article.service

import com.toyProject7.karrot.article.ArticleNotFoundException
import com.toyProject7.karrot.article.ArticlePermissionDeniedException
import com.toyProject7.karrot.article.controller.Article
import com.toyProject7.karrot.article.controller.PostArticleRequest
import com.toyProject7.karrot.article.controller.UpdateStatusRequest
import com.toyProject7.karrot.article.persistence.ArticleEntity
import com.toyProject7.karrot.article.persistence.ArticleLikesEntity
import com.toyProject7.karrot.article.persistence.ArticleLikesRepository
import com.toyProject7.karrot.article.persistence.ArticleRepository
import com.toyProject7.karrot.chatRoom.controller.ChatRoom
import com.toyProject7.karrot.chatRoom.persistence.ChatRoomEntity
import com.toyProject7.karrot.chatRoom.service.ChatRoomService
import com.toyProject7.karrot.image.persistence.ImageUrlEntity
import com.toyProject7.karrot.image.service.ImageService
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.service.UserService
import org.springframework.context.annotation.Lazy
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
    @Lazy private val chatRoomService: ChatRoomService,
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
                tag = request.tag,
                price = request.price,
                status = 0,
                location = request.location,
                imageUrls = mutableListOf(),
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
                viewCount = 0,
                isDummy = 0,
            )
        articleRepository.save(articleEntity)

        val imagePutPresingedUrls: MutableList<String> = mutableListOf()
        if (request.imageCount > 0) {
            for (number in 1..request.imageCount) {
                val imageUrlEntity: ImageUrlEntity = imageService.postImageUrl("article", articleEntity.id!!, number)

                val imagePutPresignedUrl: String = imageService.generatePutPresignedUrl(imageUrlEntity.s3)
                imagePutPresingedUrls += imagePutPresignedUrl

                imageService.generateGetPresignedUrl(imageUrlEntity)
                articleEntity.imageUrls += imageUrlEntity
            }
            articleEntity.updatedAt = Instant.now()
        }
        articleRepository.save(articleEntity)

        val article = Article.fromEntity(articleEntity)
        article.imagePresignedUrl = imagePutPresingedUrls

        return article
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
        articleEntity.tag = request.tag
        articleEntity.price = request.price
        articleEntity.location = request.location
        if (articleEntity.imageUrls.isNotEmpty()) {
            imageService.deleteImageUrl(articleEntity.imageUrls)
            articleEntity.imageUrls = mutableListOf()
        }

        val imagePutPresignedUrls: MutableList<String> = mutableListOf()
        if (request.imageCount > 0) {
            for (number in 1..request.imageCount) {
                val imageUrlEntity: ImageUrlEntity = imageService.postImageUrl("article", articleEntity.id!!, number)

                val imagePutPresignedUrl: String = imageService.generatePutPresignedUrl(imageUrlEntity.s3)
                imagePutPresignedUrls += imagePutPresignedUrl

                imageService.generateGetPresignedUrl(imageUrlEntity)
                articleEntity.imageUrls += imageUrlEntity
            }
            articleEntity.updatedAt = Instant.now()
        }
        articleRepository.save(articleEntity)

        val article = Article.fromEntity(articleEntity)
        article.imagePresignedUrl = imagePutPresignedUrls
        return article
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
        if (articleEntity.imageUrls.isNotEmpty()) {
            imageService.deleteImageUrl(articleEntity.imageUrls)
        }
        articleRepository.delete(articleEntity)
    }

    @Transactional
    fun updateStatus(
        request: UpdateStatusRequest,
        articleId: Long,
        id: String,
    ) {
        val articleEntity = getArticleEntityById(articleId)
        if (articleEntity.seller.id != id) throw ArticlePermissionDeniedException()
        articleEntity.status = request.status
    }

    @Transactional
    fun likeArticle(
        articleId: Long,
        id: String,
    ) {
        val articleEntity = articleRepository.findByIdOrNull(articleId) ?: throw ArticleNotFoundException()
        val userEntity = userService.getUserEntityById(id)
        if (articleLikesRepository.existsByUserIdAndArticleId(id, articleId)) {
            return
        }
        try {
            val articleLikesEntity =
                ArticleLikesEntity(
                    article = articleEntity,
                    user = userEntity,
                    createdAt = Instant.now(),
                    updatedAt = Instant.now(),
                )
            articleLikesRepository.save(articleLikesEntity)
        } catch (e: Exception) {
            return
        }
    }

    @Transactional
    fun unlikeArticle(
        articleId: Long,
        id: String,
    ) {
        val article = articleRepository.findByIdOrNull(articleId) ?: throw ArticleNotFoundException()
        val toBeRemoved: ArticleLikesEntity = articleLikesRepository.findByUserIdAndArticleId(id, articleId) ?: return
        article.articleLikes.remove(toBeRemoved)
        articleLikesRepository.delete(toBeRemoved)
    }

    @Transactional
    fun getArticle(
        articleId: Long,
        id: String,
    ): Article {
        val articleEntity = articleRepository.findByIdOrNull(articleId) ?: throw ArticleNotFoundException()
        articleRepository.incrementViewCount(articleId)

        refreshPresignedUrlIfExpired(listOf(articleEntity))

        val article = Article.fromEntity(articleEntity)
        article.isLiked = articleLikesRepository.existsByUserIdAndArticleId(id, article.id)
        return article
    }

    @Transactional
    fun refreshPresignedUrlIfExpired(articles: List<ArticleEntity>) {
        articles.forEach { article ->
            if (article.imageUrls.isNotEmpty() && ChronoUnit.MINUTES.between(article.updatedAt, Instant.now()) >= 10) {
                for (number in 1..article.imageUrls.size) {
                    imageService.generateGetPresignedUrl(article.imageUrls[number - 1])
                }
                article.updatedAt = Instant.now()
                articleRepository.save(article)
            }
        }
    }

    @Transactional
    fun getPreviousArticlesAndIsDummy(articleId: Long): List<ArticleEntity> {
        val articles = articleRepository.findTop10ByIdBeforeAndIsDummyOrderByIdDesc(articleId, 0)
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

    @Transactional
    fun searchArticle(
        text: String,
        articleId: Long,
    ): List<ArticleEntity> {
        val articles =
            articleRepository.findTop10ByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseAndIdLessThanOrderByIdDesc(
                text,
                text,
                articleId,
            )
        refreshPresignedUrlIfExpired(articles)
        return articles
    }

    @Transactional
    fun updateBuyer(
        articleId: Long,
        buyerId: String,
    ) {
        val articleEntity = articleRepository.findByIdOrNull(articleId) ?: throw ArticleNotFoundException()
        articleEntity.buyer = userService.getUserEntityById(buyerId)
    }

    @Transactional
    fun getArticleEntityById(articleId: Long): ArticleEntity {
        return articleRepository.findByIdOrNull(articleId) ?: throw ArticleNotFoundException()
    }

    @Transactional
    fun getItemCount(id: String): Int {
        return articleRepository.countBySellerId(id)
    }

    fun getChattingUsersByArticle(article: Article): List<User> {
        val chatRoomEntities: List<ChatRoomEntity> = chatRoomService.findAllByArticleId(article.id)
        return chatRoomEntities
            .map { chatRoomEntity -> ChatRoom.fromEntity(chatRoomEntity, "") }
            .map { chatRoom -> chatRoom.buyer }
    }
}
