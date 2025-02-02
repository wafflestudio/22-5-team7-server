package com.toyProject7.karrot.article.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ArticleRepository : JpaRepository<ArticleEntity, Long> {
    fun findTop10ByIdBeforeAndIsDummyOrderByIdDesc(
        id: Long,
        isDummy: Int,
    ): List<ArticleEntity>

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
    fun incrementViewCount(id: Long): Int

    fun countBySellerId(id: String): Int

    fun findTop10ByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseAndIdLessThanOrderByIdDesc(
        title: String,
        content: String,
        id: Long,
    ): List<ArticleEntity>

    @Modifying
    @Query("UPDATE articles a SET a.buyer = :buyer WHERE a.id = :articleId")
    fun updateBuyer(
        @Param("articleId") articleId: Long,
        @Param("buyer") buyer: UserEntity,
    )

    @Modifying
    @Query("UPDATE articles a SET a.status = :status WHERE a.id = :articleId")
    fun updateStatus(
        @Param("articleId") articleId: Long,
        @Param("status") status: Int,
    )
}
