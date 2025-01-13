package com.toyProject7.karrot.feed.controller

import com.toyProject7.karrot.feed.persistence.FeedEntity
import com.toyProject7.karrot.feed.service.FeedService
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
class FeedController(
    private val feedService: FeedService,
) {
    @PostMapping("/feed/post")
    fun postFeed(
        @RequestBody request: PostFeedRequest,
        @AuthUser user: User,
    ): ResponseEntity<Feed> {
        val feed = feedService.postFeed(request, user.id)
        return ResponseEntity.ok(feed)
    }

    @PutMapping("/feed/put/{feedId}")
    fun editFeed(
        @RequestBody request: PostFeedRequest,
        @PathVariable feedId: Long,
        @AuthUser user: User,
    ): ResponseEntity<Feed> {
        val feed = feedService.editFeed(feedId, request, user.id)
        return ResponseEntity.ok(feed)
    }

    @DeleteMapping("/feed/delete/{feedId}")
    fun deleteFeed(
        @PathVariable feedId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        feedService.deleteFeed(feedId, user.id
        return ResponseEntity.ok("Deleted Successfully")
    }

    @PostMapping("/feed/like/{feedId}")
    fun likeFeed(
        @PathVariable feedId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        feedService.likeFeed(feedId, user.id)
        return ResponseEntity.ok("Liked Successfully")
    }

    @DeleteMapping("/feed/unlike/{feedId}")
    fun unlikeFeed(
        @PathVariable feedId: Long,
        @AuthUser user: User,
    ): ResponseEntity<String> {
        feedService.unlikeArticle(feedId, user.id)
        return ResponseEntity.ok("Unliked Successfully")
    }

    @GetMapping("/feed/get/{feedId}")
    fun getFeed(
        @PathVariable feedId: Long,
    ): ResponseEntity<Feed> {
        return ResponseEntity.ok(feedService.getFeed(feedId))
    }

    @GetMapping("/feed")
    fun getFeedHome(
        @RequestParam("feedId") feedId: Long,
    ): ResponseEntity<List<FeedPreview>> {
        val feeds: List<FeedEntity> = feedService.getFeedHome(feedId)
        val response: List<FeedPreview> =
            feeds.map { feed ->
                FeedPreview.fromEntity(feed)
            }
        return ResponseEntity.ok(response)
    }


}

data class PostFeedRequest(
    val title: String,
    val content: String,
    val imageCount: Int,
)