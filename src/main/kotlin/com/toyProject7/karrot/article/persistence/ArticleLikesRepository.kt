package com.toyProject7.karrot.article.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleLikesRepository : JpaRepository<ArticleLikesEntity, String> {
    fun findTop10ByUserAndArticleIdLessThanOrderByArticleIdDesc(
        user: UserEntity,
        articleId: Long,
    ): List<ArticleLikesEntity>

    fun existsByUserIdAndArticleId(
        userId: String,
        articleId: Long,
    ): Boolean
}
