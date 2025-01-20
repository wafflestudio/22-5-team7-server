package com.toyProject7.karrot.comment.service

import com.toyProject7.karrot.comment.CommentNotFoundException
import com.toyProject7.karrot.comment.CommentWriterDoesNotMatchException
import com.toyProject7.karrot.comment.controller.Comment
import com.toyProject7.karrot.comment.controller.CommentRequest
import com.toyProject7.karrot.comment.persistence.CommentEntity
import com.toyProject7.karrot.comment.persistence.CommentLikesEntity
import com.toyProject7.karrot.comment.persistence.CommentLikesRepository
import com.toyProject7.karrot.comment.persistence.CommentRepository
import com.toyProject7.karrot.feed.service.FeedService
import com.toyProject7.karrot.user.service.UserService
import org.springframework.data.repository.findByIdOrNull
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
        feedService.saveCommentInFeed(feed, commentEntity)
        return Comment.fromEntity(commentEntity)
    }

    @Transactional
    fun editComment(
        request: CommentRequest,
        commentId: Long,
        id: String,
    ): Comment {
        val comment: CommentEntity = getCommentEntityById(commentId)
        val user = userService.getUserEntityById(id)
        if (comment.user.id != user.id) {
            throw CommentWriterDoesNotMatchException()
        }
        comment.feed.content = request.content
        comment.updatedAt = Instant.now()
        commentRepository.save(comment)
        feedService.saveCommentInFeed(comment.feed, comment)
        return Comment.fromEntity(comment)
    }

    @Transactional
    fun deleteComment(
        commentId: Long,
        id: String,
    ) {
        val comment: CommentEntity = getCommentEntityById(commentId)
        val user = userService.getUserEntityById(id)
        if (comment.user.id != user.id) {
            throw CommentWriterDoesNotMatchException()
        }
        feedService.deleteCommentInFeed(comment.feed, comment)
        commentRepository.delete(comment)
    }

    @Transactional
    fun likeComment(
        commentId: Long,
        id: String,
    ) {
        val commentEntity = commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
        val userEntity = userService.getUserEntityById(id)
        if (commentEntity.commentLikes.any { it.user.id == userEntity.id }) {
            return
        }
        val commentLikesEntity =
            commentLikesRepository.save(
                CommentLikesEntity(user = userEntity, comment = commentEntity, createdAt = Instant.now(), updatedAt = Instant.now()),
            )
        commentEntity.commentLikes += commentLikesEntity
    }

    @Transactional
    fun unlikeComment(
        commentId: Long,
        id: String,
    ) {
        val commentEntity = commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
        val userEntity = userService.getUserEntityById(id)
        val toBeRemoved: CommentLikesEntity = commentEntity.commentLikes.find { it.user.id == userEntity.id } ?: return
        commentEntity.commentLikes.remove(toBeRemoved)
        commentLikesRepository.delete(toBeRemoved)
    }

    @Transactional
    fun getCommentEntityById(commentId: Long): CommentEntity {
        return commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
    }
}
