package com.toyProject7.karrot.comment.persistence

import com.toyProject7.karrot.feed.persistence.FeedEntity
import com.toyProject7.karrot.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentRepository : JpaRepository<CommentEntity, Long> {
    @Query(
        """
        SELECT DISTINCT c.feed
        FROM comments c
        WHERE c.user = :user AND c.feed.id < :feedId
        ORDER BY c.feed.id DESC
        """,
    )
    fun findFeedsByUserComments(
        @Param("user") user: UserEntity,
        @Param("feedId") feedId: Long,
    ): List<FeedEntity>
}
