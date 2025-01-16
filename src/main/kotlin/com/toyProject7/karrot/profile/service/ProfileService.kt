package com.toyProject7.karrot.profile.service

import com.toyProject7.karrot.article.persistence.ArticleRepository
import com.toyProject7.karrot.image.persistence.ImageUrlEntity
import com.toyProject7.karrot.image.service.ImageService
import com.toyProject7.karrot.manner.controller.Manner
import com.toyProject7.karrot.profile.ProfileNotFoundException
import com.toyProject7.karrot.profile.controller.EditProfileRequest
import com.toyProject7.karrot.profile.controller.Profile
import com.toyProject7.karrot.profile.persistence.ProfileEntity
import com.toyProject7.karrot.profile.persistence.ProfileRepository
import com.toyProject7.karrot.review.controller.Review
import com.toyProject7.karrot.review.persistence.ReviewRepository
import com.toyProject7.karrot.user.UserNotFoundException
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.persistence.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val articleRepository: ArticleRepository,
    private val imageService: ImageService,
) {
    @Transactional
    fun getProfileImage(user: User): String {
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        refreshPresignedUrlIfExpired(profileEntity)
        profileRepository.save(profileEntity)
        return profileEntity.imageUrl?.presigned ?: ""
    }

    @Transactional
    fun getMyProfile(user: User): Profile {
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        refreshPresignedUrlIfExpired(profileEntity)
        profileRepository.save(profileEntity)
        val itemCount = getItemCount(user.id)
        return Profile.fromEntity(profileEntity, itemCount)
    }

    @Transactional
    fun getProfile(nickname: String): Profile {
        val userEntity = userRepository.findByNickname(nickname) ?: throw UserNotFoundException()
        val user = User.fromEntity(userEntity)
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        refreshPresignedUrlIfExpired(profileEntity)
        profileRepository.save(profileEntity)
        val itemCount = getItemCount(user.id)
        return Profile.fromEntity(profileEntity, itemCount)
    }

    @Transactional
    fun editProfile(
        user: User,
        request: EditProfileRequest,
    ): Profile {
        val userEntity = userRepository.findById(user.id).orElseThrow { UserNotFoundException() }
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        val itemCount = getItemCount(user.id)

        userEntity.nickname = request.nickname
        userEntity.location = request.location
        userRepository.save(userEntity)

        if (profileEntity.imageUrl != null) {
            val imageUrlListForDel: MutableList<ImageUrlEntity> = mutableListOf()
            imageUrlListForDel += profileEntity.imageUrl!!
            imageService.deleteImageUrl(imageUrlListForDel)
            profileEntity.imageUrl = null
        }

        val imageUrlEntity: ImageUrlEntity = imageService.postImageUrl("profile", profileEntity.id!!, 1)
        val imagePutPresignedUrl: String = imageService.generatePutPresignedUrl(imageUrlEntity.s3)
        imageService.generateGetPresignedUrl(imageUrlEntity)
        profileEntity.imageUrl = imageUrlEntity
        profileRepository.save(profileEntity)

        val profile = Profile.fromEntity(profileEntity, itemCount)
        profile.imagePresignedUrl = imagePutPresignedUrl

        return profile
    }

    @Transactional
    fun refreshPresignedUrlIfExpired(profileEntity: ProfileEntity) {
        if (profileEntity.imageUrl != null && ChronoUnit.MINUTES.between(profileEntity.updatedAt, Instant.now()) >= 10) {
            imageService.generateGetPresignedUrl(profileEntity.imageUrl!!)
            profileEntity.updatedAt = Instant.now()
            profileRepository.save(profileEntity)
        }
    }

    @Transactional
    fun getManner(nickname: String): List<Manner> {
        val userEntity = userRepository.findByNickname(nickname) ?: throw UserNotFoundException()
        val user = User.fromEntity(userEntity)
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        return Profile.fromEntity(profileEntity, 0).manners
    }

    @Transactional
    fun getPreviousReviews(
        nickname: String,
        reviewId: Long,
    ): List<Review> {
        return reviewRepository.findTop10BySellerNicknameOrBuyerNicknameAndIdBeforeOrderByCreatedAtDesc(nickname, nickname, reviewId).map {
                reviewEntity ->
            Review.fromEntity(reviewEntity)
        }
    }

    fun getItemCount(id: String): Int {
        return articleRepository.countBySellerId(id)
    }
}
