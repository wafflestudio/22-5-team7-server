package com.toyProject7.karrot.article.controller

import com.toyProject7.karrot.article.persistence.ArticleEntity
import com.toyProject7.karrot.user.controller.User
import java.time.Instant

data class Article(
    val id: Long,
    val seller: User,
    val title: String,
    val content: String,
    val price: Int,
    val status: String,
    val location: String,
    val imagePresignedUrl: List<String>,
    val createdAt: Instant,
    val likeCount: Int,
    val viewCount: Int,
) {
    companion object {
        fun fromEntity(entity: ArticleEntity): Article {
            return Article(
                id = entity.id!!,
                seller = User.fromEntity(entity.seller),
                title = entity.title,
                content = entity.content,
                price = entity.price,
                status = entity.status,
                location = entity.location,
                imagePresignedUrl = entity.imagePresignedUrl,
                createdAt = entity.createdAt,
                likeCount = entity.articleLikes.size,
                viewCount = entity.viewCount,
            )
        }
    }
}
