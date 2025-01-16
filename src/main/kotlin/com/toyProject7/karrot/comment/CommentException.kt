package com.toyProject7.karrot.comment

import com.toyProject7.karrot.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class CommentException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class CommentWriterDoesNotMatchException : CommentException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_ACCEPTABLE,
    msg = "Comment writer does not match",
)

class CommentNotFoundException : CommentException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "Comment not found",
)
