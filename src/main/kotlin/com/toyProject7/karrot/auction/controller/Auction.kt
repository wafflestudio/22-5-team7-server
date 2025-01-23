package com.toyProject7.karrot.auction.controller

import com.toyProject7.karrot.auction.persistence.AuctionEntity
import com.toyProject7.karrot.user.controller.User
import java.time.Instant

data class Auction(
    val id: Long,
    val seller: User,
    val title: String,
    val content: String,
    val tag: String,
    val startingPrice: Int,
    val currentPrice: Int,
    val status: Int,
    val location: String,
    var imagePresignedUrl: List<String>,
    val startingTime: Instant,
    val endTime: Instant,
    val likeCount: Int,
    val viewCount: Int,
    var isLiked: Boolean,
) {
    companion object {
        fun fromEntity(entity: AuctionEntity): Auction {
            return Auction(
                id = entity.id!!,
                seller = User.fromEntity(entity.seller),
                title = entity.title,
                content = entity.content,
                tag = entity.tag,
                startingPrice = entity.startingPrice,
                currentPrice = entity.currentPrice,
                status = entity.status,
                location = entity.location,
                imagePresignedUrl = entity.imageUrls.map { imageUrlEntity -> imageUrlEntity.presigned }.ifEmpty { emptyList() },
                startingTime = entity.startingTime,
                endTime = entity.endTime,
                likeCount = entity.auctionLikes.size,
                viewCount = entity.viewCount,
                isLiked = false,
            )
        }
    }
}
