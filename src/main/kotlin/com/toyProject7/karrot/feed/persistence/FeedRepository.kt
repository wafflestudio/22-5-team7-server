package com.toyProject7.karrot.feed.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface FeedRepository : JpaRepository<FeedEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM feeds r WHERE r.id = :id")
    fun findByIdWithWriteLock(id: Long): FeedEntity?

    fun findTop10ByIdBeforeOrderByIdDesc(id: Long): List<FeedEntity>

    fun findTop10ByAuthorAndIdLessThanOrderByIdDesc(
        author: UserEntity,
        id: Long,
    ): List<FeedEntity>
}
