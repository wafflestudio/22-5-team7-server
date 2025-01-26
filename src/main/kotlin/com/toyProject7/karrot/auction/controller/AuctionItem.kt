package com.toyProject7.karrot.auction.controller

import java.time.Instant

data class AuctionItem(
    val id: Long,
    val title: String,
    val startingPrice: Int,
    val currentPrice: Int,
    val status: Int,
    val location: String,
    val imagePresignedUrl: String,
    val likeCount: Int,
    val endTime: Instant,
) {
    companion object {
        fun fromAuction(auction: Auction): AuctionItem {
            return AuctionItem(
                id = auction.id,
                title = auction.title,
                startingPrice = auction.startingPrice,
                currentPrice = auction.currentPrice,
                status = auction.status,
                location = auction.seller.location,
                imagePresignedUrl = if (auction.imagePresignedUrl.isEmpty()) "" else auction.imagePresignedUrl.first(),
                likeCount = auction.likeCount,
                endTime = auction.endTime,
            )
        }
    }
}
