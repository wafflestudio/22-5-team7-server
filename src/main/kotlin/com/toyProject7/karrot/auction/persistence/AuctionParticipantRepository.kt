package com.toyProject7.karrot.auction.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AuctionParticipantRepository : JpaRepository<AuctionParticipantEntity, String> {
    @Query("SELECT ap FROM auction_participant ap JOIN ap.user u WHERE u.nickname = :nickname")
    fun findByUserNickname(
        @Param("nickname") nickname: String,
    ): List<AuctionParticipantEntity>

    fun findByUserAndAuction(
        user: UserEntity,
        auction: AuctionEntity,
    ): List<AuctionParticipantEntity>
}
