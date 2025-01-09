package com.toyProject7.karrot.article.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<ArticleEntity, Long> {
    fun findTop10ByIdBeforeOrderByCreatedAtDesc(id: Long): List<ArticleEntity>

    fun findTop10BySellerAndIdLessThanOrderByIdDesc(
        seller: UserEntity,
        id: Long,
    ): List<ArticleEntity>

    fun findTop10ByBuyerAndIdLessThanOrderByIdDesc(
        buyer: UserEntity,
        id: Long,
    ): List<ArticleEntity>
}
