package com.toyProject7.karrot.comment.controller

import com.toyProject7.karrot.comment.persistence.CommentEntity
import com.toyProject7.karrot.user.controller.User
import java.time.Instant

data class Comment(
    val id: Long,
    val user: User,
    val content: String,
    val commentLikesCount: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun fromEntity(entity: CommentEntity): Comment {
            return Comment(
                id = entity.id!!,
                user = User.fromEntity(entity.user),
                content = entity.content,
                commentLikesCount = entity.commentLikes.size,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }
}
