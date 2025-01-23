package com.toyProject7.karrot.auction.service

import com.toyProject7.karrot.article.ArticlePermissionDeniedException
import com.toyProject7.karrot.article.controller.UpdateStatusRequest
import com.toyProject7.karrot.auction.AuctionBadPriceException
import com.toyProject7.karrot.auction.AuctionNotFoundException
import com.toyProject7.karrot.auction.AuctionPermissionDeniedException
import com.toyProject7.karrot.auction.controller.Auction
import com.toyProject7.karrot.auction.controller.AuctionMessage
import com.toyProject7.karrot.auction.controller.PostAuctionRequest
import com.toyProject7.karrot.auction.persistence.AuctionEntity
import com.toyProject7.karrot.auction.persistence.AuctionLikesEntity
import com.toyProject7.karrot.auction.persistence.AuctionLikesRepository
import com.toyProject7.karrot.auction.persistence.AuctionRepository
import com.toyProject7.karrot.image.persistence.ImageUrlEntity
import com.toyProject7.karrot.image.service.ImageService
import com.toyProject7.karrot.user.service.UserService
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class AuctionService(
    private val auctionRepository: AuctionRepository,
    private val auctionLikesRepository: AuctionLikesRepository,
    private val userService: UserService,
    @Lazy private val imageService: ImageService,
) {
    @Transactional
    fun updatePrice(auctionMessage: AuctionMessage): AuctionMessage {
        val auctionEntity = auctionRepository.findByIdOrNull(auctionMessage.auctionId) ?: throw AuctionNotFoundException()
        if (auctionEntity.currentPrice >= auctionMessage.price) {
            throw AuctionBadPriceException()
        }
        val bidder = userService.getUserEntityByNickname(auctionMessage.senderNickname)

        auctionEntity.currentPrice = auctionMessage.price
        auctionEntity.bidder = bidder
        if (ChronoUnit.MINUTES.between(auctionEntity.endTime, Instant.now()) <= 1) {
            auctionEntity.endTime.plus(1, ChronoUnit.MINUTES)
        }

        return auctionMessage
    }

    @Transactional
    fun postAuction(
        request: PostAuctionRequest,
        id: String,
    ): Auction {
        val userEntity = userService.getUserEntityById(id)
        val auctionEntity =
            AuctionEntity(
                seller = userEntity,
                bidder = null,
                title = request.title,
                content = request.content,
                tag = request.tag,
                startingPrice = request.startingPrice,
                currentPrice = request.startingPrice,
                status = 0,
                location = request.location,
                imageUrls = mutableListOf(),
                startingTime = Instant.now(),
                endTime = Instant.now().plus(request.duration, ChronoUnit.MINUTES),
                updatedAt = Instant.now(),
                viewCount = 0,
            )
        auctionRepository.save(auctionEntity)

        val imagePutPresingedUrls: MutableList<String> = mutableListOf()
        if (request.imageCount > 0) {
            for (number in 1..request.imageCount) {
                val imageUrlEntity: ImageUrlEntity = imageService.postImageUrl("auction", auctionEntity.id!!, number)

                val imagePutPresignedUrl: String = imageService.generatePutPresignedUrl(imageUrlEntity.s3)
                imagePutPresingedUrls += imagePutPresignedUrl

                imageService.generateGetPresignedUrl(imageUrlEntity)
                auctionEntity.imageUrls += imageUrlEntity
            }
            auctionEntity.updatedAt = Instant.now()
        }
        auctionRepository.save(auctionEntity)

        val auction = Auction.fromEntity(auctionEntity)
        auction.imagePresignedUrl = imagePutPresingedUrls

        return auction
    }

    @Transactional
    fun deleteAuction(
        auctionId: Long,
        id: String,
    ) {
        val user = userService.getUserEntityById(id)
        val auctionEntity = auctionRepository.findByIdOrNull(auctionId) ?: throw AuctionNotFoundException()
        if (auctionEntity.seller.id != user.id) {
            throw AuctionPermissionDeniedException()
        }
        if (auctionEntity.imageUrls.isNotEmpty()) {
            imageService.deleteImageUrl(auctionEntity.imageUrls)
        }
        auctionRepository.delete(auctionEntity)
    }

    @Transactional
    fun updateStatus(
        request: UpdateStatusRequest,
        auctionId: Long,
        id: String,
    ) {
        val auctionEntity = getAuctionEntityById(auctionId)
        if (auctionEntity.seller.id != id) throw ArticlePermissionDeniedException()
        auctionEntity.status = request.status
    }

    @Transactional
    fun likeAuction(
        auctionId: Long,
        id: String,
    ) {
        val auctionEntity = auctionRepository.findByIdOrNull(auctionId) ?: throw AuctionNotFoundException()
        val userEntity = userService.getUserEntityById(id)
        if (auctionLikesRepository.existsByUserIdAndAuctionId(id, auctionId)) {
            return
        }
        try {
            val auctionLikesEntity =
                AuctionLikesEntity(
                    auction = auctionEntity,
                    user = userEntity,
                    createdAt = Instant.now(),
                    updatedAt = Instant.now(),
                )
            auctionLikesRepository.save(auctionLikesEntity)
        } catch (e: Exception) {
            return
        }
    }

    @Transactional
    fun unlikeAuction(
        auctionId: Long,
        id: String,
    ) {
        val auctionEntity = auctionRepository.findByIdOrNull(auctionId) ?: throw AuctionNotFoundException()
        val toBeRemoved: AuctionLikesEntity = auctionLikesRepository.findByUserIdAndArticleId(id, auctionId) ?: return
        auctionEntity.auctionLikes.remove(toBeRemoved)
        auctionLikesRepository.delete(toBeRemoved)
    }

    @Transactional
    fun getAuction(
        auctionId: Long,
        id: String,
    ): Auction {
        val auctionEntity = auctionRepository.findByIdOrNull(auctionId) ?: throw AuctionNotFoundException()
        auctionRepository.incrementViewCount(auctionId)

        refreshPresignedUrlIfExpired(listOf(auctionEntity))

        val auction = Auction.fromEntity(auctionEntity)
        auction.isLiked = auctionLikesRepository.existsByUserIdAndAuctionId(id, auction.id)
        return auction
    }

    @Transactional
    fun refreshPresignedUrlIfExpired(auctions: List<AuctionEntity>) {
        auctions.forEach { auction ->
            if (auction.imageUrls.isNotEmpty() && ChronoUnit.MINUTES.between(auction.updatedAt, Instant.now()) >= 10) {
                for (number in 1..auction.imageUrls.size) {
                    imageService.generateGetPresignedUrl(auction.imageUrls[number - 1])
                }
                auction.updatedAt = Instant.now()
                auctionRepository.save(auction)
            }
        }
    }

    @Transactional
    fun getPreviousAuctions(auctionId: Long): List<AuctionEntity> {
        val auctions = auctionRepository.findTop10ByIdBeforeOrderByIdDesc(auctionId)
        refreshPresignedUrlIfExpired(auctions)
        return auctions
    }

    @Transactional
    fun getAuctionEntityById(auctionId: Long): AuctionEntity {
        return auctionRepository.findByIdOrNull(auctionId) ?: throw AuctionNotFoundException()
    }
}
