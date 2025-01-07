package com.toyProject7.karrot.review

import com.toyProject7.karrot.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class ReviewException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class ReviewContentLengthOutOfRangeException :
    ReviewException(
        errorCode = 0,
        httpStatusCode = HttpStatus.BAD_REQUEST,
        msg = "Review content length out of range",
    )

class UserNotFoundException : ReviewException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "User not found",
)
