package com.toyProject7.karrot.feed.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FeedRepository : JpaRepository<FeedEntity, Long> {
    fun findTop10ByIdBeforeOrderByIdDesc(id: Long): List<FeedEntity>

    fun findTop10ByAuthorAndIdLessThanOrderByIdDesc(
        author: UserEntity,
        id: Long,
    ): List<FeedEntity>

    @Modifying
    @Query("UPDATE feeds f SET f.viewCount = f.viewCount + 1 WHERE f.id = :id")
    fun incrementViewCount(id: Long): Int

    fun findTop10ByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseAndIdLessThanOrderByIdDesc(
        title: String,
        content: String,
        id: Long,
    ): List<FeedEntity>
}
