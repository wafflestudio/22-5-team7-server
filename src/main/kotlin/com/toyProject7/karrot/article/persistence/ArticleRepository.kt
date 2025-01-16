package com.toyProject7.karrot.article.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
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

    fun countBySellerId(id: String): Int

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM feeds r WHERE r.id = :id")
    fun findByIdWithWriteLock(id: Long): ArticleEntity?
}
