package com.toyProject7.karrot.feed.persistence

import com.toyProject7.karrot.feed.controller.Feed
import com.toyProject7.karrot.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FeedRepository : JpaRepository<FeedEntity, Long> {
    fun findTop10ByIdBeforeOrderByCreatedAtDesc(id: Long): List<FeedEntity>

    fun findTop10ByAuthorAndIdLessThanOrderByIdDesc(author: UserEntity, id: Long): List<FeedEntity>
}
