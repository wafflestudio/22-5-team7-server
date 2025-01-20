package com.toyProject7.karrot.feed.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FeedLikesRepository : JpaRepository<FeedLikesEntity, String> {
    fun findTop10ByUserAndFeedIdLessThanOrderByFeedIdDesc(
        user: UserEntity,
        feedId: Long,
    ): List<FeedLikesEntity>
}
