package com.toyProject7.karrot.article.controller

import java.time.Instant

data class Item(
    val id: String,
    val title: String,
    val location: String,
    val createdAt: Instant,
    val price: Int,
    val likeCount: Int,
) {
    companion object {
        fun fromArticle(article: Article): Item {
            return Item(
                id = article.id,
                title = article.title,
                location = article.location,
                createdAt = article.createdAt,
                price = article.price,
                likeCount = article.likeCount,
            )
        }
    }
}
