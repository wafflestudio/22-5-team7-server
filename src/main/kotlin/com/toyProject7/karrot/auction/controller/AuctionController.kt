package com.toyProject7.karrot.auction.controller

import com.toyProject7.karrot.article.controller.UpdateStatusRequest
import com.toyProject7.karrot.auction.persistence.AuctionEntity
import com.toyProject7.karrot.auction.service.AuctionService
import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
        val auction = auctionService.postAuction(request, user.id)
        return ResponseEntity.ok(auction)
    }

    @DeleteMapping("/auction/delete/{auctionId}")
    fun deleteAuction(
        @PathVariable auctionId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        auctionService.deleteAuction(auctionId, user.id)
        return ResponseEntity.ok("Deleted Successfully")
    }

    @PutMapping("/auction/status/{auctionId}")
    fun updateStatus(
        @RequestBody request: UpdateStatusRequest,
        @PathVariable auctionId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        auctionService.updateStatus(request, auctionId, user.id)
        return ResponseEntity.ok("Status Updated Successfully")
    }

    @PostMapping("/auction/like/{auctionId}")
    fun likeAuction(
        @PathVariable auctionId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        auctionService.likeAuction(auctionId, user.id)
        return ResponseEntity.ok("Liked Successfully")
    }

    @DeleteMapping("/auction/unlike/{auctionId}")
    fun unlikeAuction(
        @PathVariable auctionId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        auctionService.unlikeAuction(auctionId, user.id)
        return ResponseEntity.ok("Unliked Successfully")
    }

    @GetMapping("/auction/get/{auctionId}")
    fun getAuction(
        @PathVariable auctionId: Long,
        @AuthUser user: User,
    ): ResponseEntity<Auction> {
        val auction = auctionService.getAuction(auctionId, user.id)
        return ResponseEntity.ok(auction)
    }

    @GetMapping("/auctions")
    fun getPreviousAuctions(
        @RequestParam("auctionId") auctionId: Long,
    ): ResponseEntity<List<AuctionItem>> {
        val auctions: List<AuctionEntity> = auctionService.getPreviousAuctions(auctionId)
        val response: List<AuctionItem> =
            auctions.map { auction ->
                AuctionItem.fromAuction(Auction.fromEntity(auction))
            }
        return ResponseEntity.ok(response)
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
