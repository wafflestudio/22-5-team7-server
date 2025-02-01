package com.toyProject7.karrot.chatRoom

import com.toyProject7.karrot.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class ChatRoomException(
    errorCode: Int,
    httpStatusCode: HttpStatusCode,
    msg: String,
    cause: Throwable? = null,
) : DomainException(errorCode, httpStatusCode, msg, cause)

class ChatRoomNotFoundException : ChatRoomException(
    errorCode = 0,
    httpStatusCode = HttpStatus.NOT_FOUND,
    msg = "ChatRoom not found",
)

class ThisRoomIsNotYoursException : ChatRoomException(
    errorCode = 0,
    httpStatusCode = HttpStatus.FORBIDDEN,
    msg = "Not your room",
)

class SellerCreateChatRoomWithSellerException : ChatRoomException(
    errorCode = 0,
    httpStatusCode = HttpStatus.BAD_REQUEST,
    msg = "Seller can't make chatRoom with himself",
)
