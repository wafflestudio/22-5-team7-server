package com.toyProject7.karrot.feed.service

import com.toyProject7.karrot.comment.persistence.CommentEntity
import com.toyProject7.karrot.feed.FeedNotFoundException
import com.toyProject7.karrot.feed.FeedPermissionDeniedException
import com.toyProject7.karrot.feed.controller.Feed
import com.toyProject7.karrot.feed.controller.PostFeedRequest
import com.toyProject7.karrot.feed.persistence.FeedEntity
import com.toyProject7.karrot.feed.persistence.FeedLikesEntity
import com.toyProject7.karrot.feed.persistence.FeedLikesRepository
import com.toyProject7.karrot.feed.persistence.FeedRepository
import com.toyProject7.karrot.image.persistence.ImageUrlEntity
import com.toyProject7.karrot.image.service.ImageService
import com.toyProject7.karrot.user.service.UserService
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class FeedService(
    private val feedRepository: FeedRepository,
    private val feedLikesRepository: FeedLikesRepository,
    private val userService: UserService,
    @Lazy private val imageService: ImageService,
) {
    @Transactional
    fun postFeed(
        request: PostFeedRequest,
        id: String,
    ): Feed {
        val user = userService.getUserEntityById(id)
        val feedEntity =
            FeedEntity(
                author = user,
                title = request.title,
                content = request.content,
                imageUrls = mutableListOf(),
                feedLikes = mutableListOf(),
                feedComments = mutableListOf(),
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
                viewCount = 1,
            )
        feedRepository.save(feedEntity)
        val imagePutPresignedUrls: MutableList<String> = mutableListOf()
        if (request.imageCount > 0) {
            for (number in 1..request.imageCount) {
                val imageUrlEntity: ImageUrlEntity = imageService.postImageUrl("feed", feedEntity.id!!, number)

                val imagePutPresignedUrl: String = imageService.generatePutPresignedUrl(imageUrlEntity.s3)
                imagePutPresignedUrls += imagePutPresignedUrl

                imageService.generateGetPresignedUrl(imageUrlEntity)
                feedEntity.imageUrls += imageUrlEntity
            }
            feedEntity.updatedAt = Instant.now()
        }
        feedRepository.save(feedEntity)
        val feed = Feed.fromEntity(feedEntity)
        feed.imagePresignedUrl = imagePutPresignedUrls
        return feed
    }

    @Transactional
    fun editFeed(
        feedId: Long,
        request: PostFeedRequest,
        id: String,
    ): Feed {
        val user = userService.getUserEntityById(id)
        val feedEntity = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        if (feedEntity.author.id != user.id) {
            throw FeedPermissionDeniedException()
        }
        feedEntity.title = request.title
        feedEntity.content = request.content
        if (feedEntity.imageUrls.isNotEmpty()) {
            imageService.deleteImageUrl(feedEntity.imageUrls)
            feedEntity.imageUrls = mutableListOf()
        }
        val imagePutPresignedUrls: MutableList<String> = mutableListOf()
        if (request.imageCount > 0) {
            for (number in 1..request.imageCount) {
                val imageUrlEntity: ImageUrlEntity = imageService.postImageUrl("feed", feedEntity.id!!, number)

                val imagePutPresignedUrl: String = imageService.generatePutPresignedUrl(imageUrlEntity.s3)
                imagePutPresignedUrls += imagePutPresignedUrl

                imageService.generateGetPresignedUrl(imageUrlEntity)
                feedEntity.imageUrls += imageUrlEntity
            }
            feedEntity.updatedAt = Instant.now()
        }
        feedEntity.viewCount += 1
        feedRepository.save(feedEntity)
        val feed = Feed.fromEntity(feedEntity)
        feed.imagePresignedUrl = imagePutPresignedUrls
        return feed
    }

    @Transactional
    fun deleteFeed(
        feedId: Long,
        id: String,
    ) {
        val user = userService.getUserEntityById(id)
        val feedEntity = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        if (feedEntity.author.id != user.id) {
            throw FeedPermissionDeniedException()
        }
        if (feedEntity.imageUrls.isNotEmpty()) {
            imageService.deleteImageUrl(feedEntity.imageUrls)
        }
        feedRepository.delete(feedEntity)
    }

    @Transactional
    fun likeFeed(
        feedId: Long,
        id: String,
    ) {
        val feedEntity = feedRepository.findByIdWithWriteLock(feedId) ?: throw FeedNotFoundException()
        val userEntity = userService.getUserEntityById(id)
        if (feedEntity.feedLikes.any { it.user.id == userEntity.id }) {
            return
        }
        val feedLikesEntity =
            feedLikesRepository.save(
                FeedLikesEntity(feed = feedEntity, user = userEntity, createdAt = Instant.now(), updatedAt = Instant.now()),
            )
        feedEntity.feedLikes += feedLikesEntity
        feedRepository.save(feedEntity)
    }

    @Transactional
    fun unlikeFeed(
        feedId: Long,
        id: String,
    ) {
        val feedEntity = feedRepository.findByIdWithWriteLock(feedId) ?: throw FeedNotFoundException()
        val userEntity = userService.getUserEntityById(id)
        val toBeRemoved: FeedLikesEntity = feedEntity.feedLikes.find { it.user.id == userEntity.id } ?: return
        feedEntity.feedLikes -= toBeRemoved
        feedLikesRepository.delete(toBeRemoved)
        feedRepository.save(feedEntity)
    }

    @Transactional
    fun getFeed(feedId: Long): Feed {
        val feedEntity = feedRepository.findByIdWithWriteLock(feedId) ?: throw FeedNotFoundException()
        val feed: List<FeedEntity> = listOf(feedEntity)
        refreshPresignedUrlIfExpired(feed)
        feedEntity.viewCount += 1
        feedRepository.save(feedEntity)
        return Feed.fromEntity(feedEntity)
    }

    @Transactional
    fun getFeedHome(feedId: Long): List<FeedEntity> {
        val feeds = feedRepository.findTop10ByIdBeforeOrderByIdDesc(feedId)
        refreshPresignedUrlIfExpired(feeds)
        return feeds
    }

    @Transactional
    fun refreshPresignedUrlIfExpired(feeds: List<FeedEntity>) {
        feeds.forEach { feed ->
            if (feed.imageUrls.isNotEmpty() && ChronoUnit.MINUTES.between(feed.updatedAt, Instant.now()) >= 10) {
                for (number in 1..feed.imageUrls.size) {
                    imageService.generateGetPresignedUrl(feed.imageUrls[number - 1])
                }
                feed.updatedAt = Instant.now()
                feedRepository.save(feed)
            }
        }
    }

    @Transactional
    fun getFeedEntityById(feedId: Long): FeedEntity {
        return feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
    }

    @Transactional
    fun saveCommentInFeed(
        feed: FeedEntity,
        comment: CommentEntity,
    ) {
        feed.feedComments += comment
        feedRepository.save(feed)
    }
}
