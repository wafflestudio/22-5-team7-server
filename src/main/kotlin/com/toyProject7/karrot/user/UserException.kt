package com.toyProject7.karrot.user

import com.toyProject7.karrot.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class UserException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class SignUpUserIdConflictException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.CONFLICT,
    msg = "UserId conflict",
)

class SignUpNicknameConflictException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.CONFLICT,
    msg = "Nickname conflict",
)

class SignUpBadUserIdException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "Bad userId, User ID must be 5-20 characters",
)

class SignUpBadPasswordException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "Bad password, password must be 8-16 characters",
)

class SignUpBadNicknameException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "Bad nickname, User ID must be 2-10 characters",
)

class SignUpInvalidEmailException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "Invalid email, email must be in a valid format",
)

class SignInUserNotFoundException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.UNAUTHORIZED,
    msg = "User not found",
)

class SignInInvalidPasswordException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.UNAUTHORIZED,
    msg = "Invalid password",
)

class AuthenticateException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.UNAUTHORIZED,
    msg = "Authenticate failed",
)

class UserNotFoundException : UserException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "User not found",
)
