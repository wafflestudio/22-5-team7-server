package com.toyProject7.karrot.feed.controller

import com.toyProject7.karrot.feed.persistence.FeedEntity
import java.time.Instant

data class FeedPreview(
    val id: Long,
    val authorLocation: String,
    val title: String,
    val content: String,
    val imagePresignedUrl: String,
    val likeCount: Int,
    val commentCount: Int,
    val createdAt: Instant,
    val viewCount: Int,
) {
    companion object {
        fun fromEntity(entity: FeedEntity): FeedPreview {
            return FeedPreview(
                id = entity.id!!,
                authorLocation = entity.author.location,
                title = entity.title,
                content = entity.content.take(40),
                imagePresignedUrl = if (entity.imageUrls.isEmpty()) "" else entity.imageUrls.first().presigned,
                likeCount = entity.feedLikes.size,
                commentCount = entity.feedComments.size,
                createdAt = entity.createdAt,
                viewCount = entity.viewCount,
            )
        }
    }
}
