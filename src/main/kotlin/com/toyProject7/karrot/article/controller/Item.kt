package com.toyProject7.karrot.article.controller

import java.time.Instant

// article's preview information
data class Item(
    val id: Long,
    val title: String,
    val price: Int,
    val status: Int,
    val location: String,
    val imagePresignedUrl: String,
    val createdAt: Instant,
    val likeCount: Int,
    val chatCount: Int,
) {
    companion object {
        fun fromArticle(
            article: Article,
            chatCount: Int,
        ): Item {
            return Item(
                id = article.id,
                title = article.title,
                price = article.price,
                status = article.status,
                location = article.seller.location,
                imagePresignedUrl = if (article.imagePresignedUrl.isEmpty()) "" else article.imagePresignedUrl.first(),
                createdAt = article.createdAt,
                likeCount = article.likeCount,
                chatCount = chatCount,
            )
        }
    }
}
