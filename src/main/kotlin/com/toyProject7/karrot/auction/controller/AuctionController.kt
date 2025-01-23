package com.toyProject7.karrot.auction.controller

import com.toyProject7.karrot.auction.service.AuctionService
import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AuctionController(
    private val auctionService: AuctionService,
) {
    @PostMapping("/auction/post")
    fun postAuction(
        @RequestBody request: PostAuctionRequest,
        @AuthUser user: User,
    ): ResponseEntity<Auction> {
        val auction = auctionService.postAuction(request, user)
        return ResponseEntity.ok(auction)
    }
}

data class PostAuctionRequest(
    val title: String,
    val content: String,
    val tag: String,
    val startingPrice: Int,
    val duration: Long,
    val location: String,
    val imageCount: Int,
)
