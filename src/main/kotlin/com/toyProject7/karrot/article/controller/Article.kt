package com.toyProject7.karrot.article.controller

import com.toyProject7.karrot.article.persistence.ArticleEntity
import com.toyProject7.karrot.user.controller.User
import java.time.Instant

data class Article(
    val id: String,
    val seller: User,
    val buyer: User?,
    val title: String,
    val content: String,
    val price: Int,
    val isSelled: Boolean,
    val createdAt: Instant,
    val likeCount: Int,
) {
    companion object {
        fun fromEntity(entity: ArticleEntity): Article {
            return Article(
                id = entity.id!!,
                seller = User.fromEntity(entity.seller),
                buyer = entity.buyer?.let { User.fromEntity(it) },
                title = entity.title,
                content = entity.content,
                price = entity.price,
                isSelled = entity.isSelled,
                createdAt = entity.createdAt,
                likeCount = entity.articleLikes.size,
            )
        }
    }
}
