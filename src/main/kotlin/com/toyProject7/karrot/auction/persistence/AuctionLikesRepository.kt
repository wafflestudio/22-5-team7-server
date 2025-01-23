package com.toyProject7.karrot.auction.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface AuctionLikesRepository : JpaRepository<AuctionLikesEntity, String>
