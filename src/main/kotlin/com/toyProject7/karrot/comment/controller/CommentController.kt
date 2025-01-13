package com.toyProject7.karrot.comment.controller

import com.toyProject7.karrot.comment.service.CommentService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CommentController(
    private val commentService: CommentService,
) {

}
