package com.toyProject7.karrot.profile

import com.toyProject7.karrot.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class ProfileException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class ProfileNotFoundException : ProfileException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "Profile not found",
)

class ProfileEditNicknameConflictException : ProfileException(
    errorCode = 0,
    httpStatusCode = HttpStatus.CONFLICT,
    msg = "Nickname conflict",
)
