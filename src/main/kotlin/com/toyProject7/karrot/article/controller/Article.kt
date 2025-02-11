package com.toyProject7.karrot.article.controller

import com.toyProject7.karrot.article.persistence.ArticleEntity
import com.toyProject7.karrot.user.controller.User
import java.time.Instant

data class Article(
    val id: Long,
    val seller: User,
    val buyer: User?,
    val title: String,
    val content: String,
    val tag: String,
    val price: Int,
    val status: Int,
    val location: String,
    var imagePresignedUrl: List<String>,
    val createdAt: Instant,
    val likeCount: Int,
    val viewCount: Int,
    var isLiked: Boolean,
) {
    companion object {
        fun fromEntity(entity: ArticleEntity): Article {
            return Article(
                id = entity.id!!,
                seller = User.fromEntity(entity.seller),
                buyer = entity.buyer?.let { User.fromEntity(it) },
                title = entity.title,
                content = entity.content,
                tag = entity.tag,
                price = entity.price,
                status = entity.status,
                location = entity.location,
                imagePresignedUrl = entity.imageUrls.map { imageUrlEntity -> imageUrlEntity.presigned }.ifEmpty { emptyList() },
                createdAt = entity.createdAt,
                likeCount = entity.articleLikes.size,
                viewCount = entity.viewCount,
                isLiked = false,
            )
        }
    }
}
