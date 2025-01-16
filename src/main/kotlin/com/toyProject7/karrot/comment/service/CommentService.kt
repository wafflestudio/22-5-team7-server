package com.toyProject7.karrot.comment.service

import com.toyProject7.karrot.comment.controller.Comment
import com.toyProject7.karrot.comment.controller.CommentRequest
import com.toyProject7.karrot.comment.persistence.CommentEntity
import com.toyProject7.karrot.comment.persistence.CommentLikesRepository
import com.toyProject7.karrot.comment.persistence.CommentRepository
import com.toyProject7.karrot.feed.service.FeedService
import com.toyProject7.karrot.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val commentLikesRepository: CommentLikesRepository,
    private val userService: UserService,
    private val feedService: FeedService,
) {
    @Transactional
    fun postComment(
        request: CommentRequest,
        feedId: Long,
        id: String,
    ): Comment {
        val feed = feedService.getFeedEntityById(feedId)
        val user = userService.getUserEntityById(id)
        val commentEntity =
            CommentEntity(
                user = user,
                feed = feed,
                content = request.content,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            )
        commentRepository.save(commentEntity)
        return Comment.fromEntity(commentEntity)
    }
}
