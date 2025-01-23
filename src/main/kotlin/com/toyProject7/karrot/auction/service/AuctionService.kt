package com.toyProject7.karrot.auction.service

import com.toyProject7.karrot.article.ArticlePermissionDeniedException
import com.toyProject7.karrot.article.controller.UpdateStatusRequest
import com.toyProject7.karrot.auction.AuctionNotFoundException
import com.toyProject7.karrot.auction.AuctionPermissionDeniedException
import com.toyProject7.karrot.auction.controller.Auction
import com.toyProject7.karrot.auction.controller.AuctionMessage
import com.toyProject7.karrot.auction.controller.PostAuctionRequest
import com.toyProject7.karrot.auction.persistence.AuctionEntity
import com.toyProject7.karrot.auction.persistence.AuctionLikesRepository
import com.toyProject7.karrot.auction.persistence.AuctionRepository
import com.toyProject7.karrot.image.persistence.ImageUrlEntity
import com.toyProject7.karrot.image.service.ImageService
import com.toyProject7.karrot.user.service.UserService
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
    private val imageService: ImageService,
) {
    @Transactional
    fun updatePrice(auctionMessage: AuctionMessage): AuctionMessage {
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
    fun getAuctionEntityById(auctionId: Long): AuctionEntity {
        return auctionRepository.findByIdOrNull(auctionId) ?: throw AuctionNotFoundException()
    }
}
