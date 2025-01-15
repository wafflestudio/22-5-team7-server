package com.toyProject7.karrot.feed

import com.toyProject7.karrot.DomainException
import org.springframework.http.HttpStatus
import software.amazon.awssdk.http.HttpStatusCode

sealed class FeedException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class FeedNotFoundException : FeedException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "Feed not found",
)

class FeedPermissionDeniedException : FeedException(
    errorCode = 0,
    httpStatusCode = HttpStatus.UNAUTHORIZED,
    msg = "Permission denied",
)
