package com.toyProject7.karrot.comment.controller

import com.toyProject7.karrot.comment.service.CommentService
import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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
}

data class CommentRequest(
    val content: String,
)
