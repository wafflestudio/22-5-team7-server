package com.toyProject7.karrot.feed.service

import com.toyProject7.karrot.comment.persistence.CommentEntity
import com.toyProject7.karrot.comment.service.CommentService
import com.toyProject7.karrot.feed.FeedNotFoundException
import com.toyProject7.karrot.feed.FeedPermissionDeniedException
import com.toyProject7.karrot.feed.controller.Feed
import com.toyProject7.karrot.feed.controller.PostFeedRequest
import com.toyProject7.karrot.feed.controller.SearchRequest
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
    @Lazy private val commentService: CommentService,
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
                tag = request.tag,
                imageUrls = mutableListOf(),
                feedLikes = mutableListOf(),
                feedComments = mutableListOf(),
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
                viewCount = 0,
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
        feedEntity.tag = request.tag
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
        val feedEntity = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        val userEntity = userService.getUserEntityById(id)
        if (feedEntity.feedLikes.any { it.user.id == userEntity.id }) {
            return
        }
        val feedLikesEntity =
            feedLikesRepository.save(
                FeedLikesEntity(feed = feedEntity, user = userEntity, createdAt = Instant.now(), updatedAt = Instant.now()),
            )
        feedEntity.feedLikes += feedLikesEntity
    }

    @Transactional
    fun unlikeFeed(
        feedId: Long,
        id: String,
    ) {
        val feedEntity = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        val toBeRemoved: FeedLikesEntity = feedLikesRepository.findByUserIdAndFeedId(id, feedId) ?: return
        feedEntity.feedLikes.remove(toBeRemoved)
        feedLikesRepository.delete(toBeRemoved)
    }

    @Transactional
    fun getFeed(
        feedId: Long,
        id: String,
    ): Feed {
        val feedEntity = feedRepository.findByIdOrNull(feedId) ?: throw FeedNotFoundException()
        feedRepository.incrementViewCount(feedId)

        refreshPresignedUrlIfExpired(listOf(feedEntity))

        val feed = Feed.fromEntity(feedEntity)
        feed.isLiked = feedLikesRepository.existsByUserIdAndFeedId(id, feed.id)

        feed.commentList.forEach { comment ->
            comment.isLiked = commentService.userLikesComment(id, comment.id)
        }

        return feed
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
    fun getFeedsByAuthor(
        id: String,
        feedId: Long,
    ): List<FeedEntity> {
        val author = userService.getUserEntityById(id)
        val feeds = feedRepository.findTop10ByAuthorAndIdLessThanOrderByIdDesc(author, feedId)
        refreshPresignedUrlIfExpired(feeds)
        return feeds
    }

    @Transactional
    fun getFeedsThatUserLikes(
        id: String,
        feedId: Long,
    ): List<FeedEntity> {
        val userEntity = userService.getUserEntityById(id)
        val feeds =
            feedLikesRepository.findTop10ByUserAndFeedIdLessThanOrderByFeedIdDesc(
                userEntity,
                feedId,
            ).map { it.feed }
        refreshPresignedUrlIfExpired(feeds)
        return feeds
    }

    @Transactional
    fun getFeedsThatUserComments(
        id: String,
        feedId: Long,
    ): List<FeedEntity> {
        val comments: List<CommentEntity> = commentService.getCommentsByUser(id)
        val feeds: List<FeedEntity> =
            comments
                .map { it.feed }
                .filter { it.id!! < feedId }
                .distinctBy { it.id }
                .sortedByDescending { it.id }
        if (feeds.size < 10) return feeds
        return feeds.subList(0, 10)
    }

    @Transactional
    fun getPopularFeeds(feedId: Long): List<FeedEntity> {
        val feeds = feedRepository.findTop10ByIdLessThanOrderByViewCountDescIdDesc(feedId)
        refreshPresignedUrlIfExpired(feeds)
        return feeds
    }

    @Transactional
    fun searchFeed(
        request: SearchRequest,
        feedId: Long,
    ): List<FeedEntity> {
        val text = request.text
        val feeds =
            feedRepository.findTop10ByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseAndIdLessThanOrderByIdDesc(text, text, feedId)
        refreshPresignedUrlIfExpired(feeds)
        return feeds
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

    @Transactional
    fun deleteCommentInFeed(
        feed: FeedEntity,
        comment: CommentEntity,
    ) {
        feed.feedComments -= comment
        feedRepository.save(feed)
    }
}
