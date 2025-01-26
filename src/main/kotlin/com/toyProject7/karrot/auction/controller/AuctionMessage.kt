package com.toyProject7.karrot.auction.controller

import java.time.Instant

data class AuctionMessage(
    val auctionId: Long,
    val senderNickname: String,
    val price: Int,
    val createdAt: Instant,
)
