package com.toyProject7.karrot.article

import com.toyProject7.karrot.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class ArticleException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class ArticleNotFoundException : ArticleException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "Article not found",
)

class ArticlePermissionDeniedException : ArticleException(
    errorCode = 0,
    httpStatusCode = HttpStatus.UNAUTHORIZED,
    msg = "Permission denied",
)

class ArticleIsNotEndException : ArticleException(
    errorCode = 0,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "Article isn't end.",
)

class YouAreNotBuyerOrSellerException : ArticleException(
    errorCode = 0,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "You are not buyer or seller",
)
