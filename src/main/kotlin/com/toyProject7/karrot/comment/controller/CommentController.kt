package com.toyProject7.karrot.comment.controller

import com.toyProject7.karrot.comment.service.CommentService
import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CommentController(
    private val commentService: CommentService,
) {
    @PostMapping("/comment/post/{feedId}")
    fun postComment(
        @RequestBody request: CommentRequest,
        @PathVariable("feedId") feedId: Long,
        @AuthUser user: User,
    ): ResponseEntity<Comment> {
        val comment = commentService.postComment(request, feedId, user.id)
        return ResponseEntity.ok(comment)
    }

    @PutMapping("/comment/edit/{commentId}")
    fun editComment(
        @RequestBody request: CommentRequest,
        @PathVariable("commentId") commentId: Long,
        @AuthUser user: User,
    ): ResponseEntity<Comment> {
        val comment = commentService.editComment(request, commentId, user.id)
        return ResponseEntity.ok(comment)
    }

    @DeleteMapping("/comment/delete/{commentId}")
    fun deleteComment(
        @PathVariable("commentId") commentId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        commentService.deleteComment(commentId, user.id)
        return ResponseEntity.ok("Deleted Successfully")
    }

    @PostMapping("/comment/like/{commentId}")
    fun likeComment(
        @PathVariable("commentId") commentId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        commentService.likeComment(commentId, user.id)
        return ResponseEntity.ok("Liked Successfully")
    }

    @DeleteMapping("/comment/unlike/{commentId}")
    fun unlikeComment(
        @PathVariable("commentId") commentId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        commentService.unlikeComment(commentId, user.id)
        return ResponseEntity.ok("Unliked Successfully")
    }
}

data class CommentRequest(
    val content: String,
)
