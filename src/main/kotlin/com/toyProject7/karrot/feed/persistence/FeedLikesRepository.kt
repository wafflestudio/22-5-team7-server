package com.toyProject7.karrot.feed.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FeedLikesRepository : JpaRepository<FeedLikesEntity, String> {
    fun findTop10ByUserAndFeedIdLessThanOrderByFeedIdDesc(
        user: UserEntity,
        feedId: Long,
    ): List<FeedLikesEntity>

    fun existsByUserIdAndFeedId(
        userId: String,
        feedId: Long,
    ): Boolean

    @Query("SELECT fe FROM feed_likes fe WHERE fe.user.id = :userId AND fe.feed.id = :feedId")
    fun findByUserIdAndFeedId(
        @Param("userId") userId: String,
        @Param("feedId") feedId: Long,
    ): FeedLikesEntity?
}
