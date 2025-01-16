package com.toyProject7.karrot.article.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ArticleRepository : JpaRepository<ArticleEntity, Long> {
    fun findTop10ByIdBeforeOrderByIdDesc(id: Long): List<ArticleEntity>

    fun findTop10BySellerAndIdLessThanOrderByIdDesc(
        seller: UserEntity,
        id: Long,
    ): List<ArticleEntity>

    fun findTop10ByBuyerAndIdLessThanOrderByIdDesc(
        buyer: UserEntity,
        id: Long,
    ): List<ArticleEntity>

    @Modifying
    @Query("UPDATE articles a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    fun incrementViewCount(id: Long): Long

    fun countBySellerId(id: String): Int
}
