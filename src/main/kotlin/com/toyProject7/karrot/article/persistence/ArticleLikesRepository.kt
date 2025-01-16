package com.toyProject7.karrot.article.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ArticleLikesRepository : JpaRepository<ArticleLikesEntity, String> {
    fun findTop10ByUserAndArticleIdLessThanOrderByArticleIdDesc(
        user: UserEntity,
        articleId: Long,
    ): List<ArticleLikesEntity>

    fun existsByUserIdAndArticleId(
        userId: String,
        articleId: Long,
    ): Boolean

    @Query("SELECT al FROM article_likes al WHERE al.user.id = :userId AND al.article.id = :articleId")
    fun findByUserIdAndArticleId(
        @Param("userId") userId: String,
        @Param("articleId") articleId: Long,
    ): ArticleLikesEntity?
}
