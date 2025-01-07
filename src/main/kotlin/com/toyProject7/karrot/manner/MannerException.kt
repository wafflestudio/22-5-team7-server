package com.toyProject7.karrot.manner

import com.toyProject7.karrot.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class MannerException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class UserNotFoundException : MannerException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "User not found",
)
