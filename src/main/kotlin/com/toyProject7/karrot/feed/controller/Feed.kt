package com.toyProject7.karrot.feed.controller

import com.toyProject7.karrot.comment.controller.Comment
import com.toyProject7.karrot.feed.persistence.FeedEntity
import com.toyProject7.karrot.user.controller.User
import java.time.Instant

data class Feed(
    val id: Long,
    val author: User,
    val title: String,
    val content: String,
    val imagePresignedUrls: List<String>,
    val likeCount: Int,
    val commentList: List<Comment>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val viewCount: Int,
) {
    companion object {
        fun fromEntity(entity: FeedEntity): Feed {
            return Feed(
                id = entity.id!!,
                author = User.fromEntity(entity.author),
                title = entity.title,
                content = entity.content,
                imagePresignedUrls = entity.imagePresignedUrls.map { imageUrlEntity -> imageUrlEntity.url }.ifEmpty { emptyList() },
                likeCount = entity.feedLikes.size,
                commentList = entity.feedComments.map { feedCommentEntity -> Comment.fromEntity(feedCommentEntity) },
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                viewCount = entity.viewCount,
            )
        }
    }
}
