package com.toyProject7.karrot.comment.service

import com.toyProject7.karrot.comment.persistence.CommentLikesRepository
import com.toyProject7.karrot.comment.persistence.CommentRepository
import org.springframework.stereotype.Service

@Service
class CommentService(
    val commentRepository: CommentRepository,
    val commentLikesRepository: CommentLikesRepository,
) {
}