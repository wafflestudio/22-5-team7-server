package com.toyProject7.karrot.article.controller

import java.time.Instant

// article's preview information
data class Item(
    val id: Long,
    val title: String,
    val price: Int,
    val status: String,
    val location: String,
    val createdAt: Instant,
    val likeCount: Int,
) {
    companion object {
        fun fromArticle(article: Article): Item {
            return Item(
                id = article.id,
                title = article.title,
                price = article.price,
                status = article.status,
                location = article.location,
                createdAt = article.createdAt,
                likeCount = article.likeCount,
            )
        }
    }
}
