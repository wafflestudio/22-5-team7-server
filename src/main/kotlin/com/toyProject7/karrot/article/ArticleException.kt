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

class S3UrlListIsEmptyException() : ArticleException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_MODIFIED,
    msg = "S3 URL List should not be empty",
)

class PresignedUrlListIsEmptyException : ArticleException(
    errorCode = 0,
    httpStatusCode = HttpStatus.GONE,
    msg = "Presigned URL List should not be empty",
)
