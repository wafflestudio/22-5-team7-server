package com.toyProject7.karrot.manner.service

import com.toyProject7.karrot.manner.controller.MannerType
import com.toyProject7.karrot.manner.persistence.MannerEntity
import com.toyProject7.karrot.manner.persistence.MannerRepository
import com.toyProject7.karrot.profile.ProfileNotFoundException
import com.toyProject7.karrot.profile.controller.Profile
import com.toyProject7.karrot.profile.persistence.ProfileRepository
import com.toyProject7.karrot.user.UserNotFoundException
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.persistence.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MannerService(
    private val mannerRepository: MannerRepository,
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
) {
    @Transactional
    fun increaseMannerCount(
        nickname: String,
        mannerType: MannerType,
    ) {
        val userEntity = userRepository.findByNickname(nickname) ?: throw UserNotFoundException()
        val user = User.fromEntity(userEntity)
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        val profile = Profile.fromEntity(profileEntity, 0)

        val mannerEntity = mannerRepository.findByProfileIdAndMannerType(profile.id, mannerType)
        if (mannerEntity != null) {
            mannerEntity.count++
            mannerRepository.save(mannerEntity)
        } else {
            val newMannerEntity =
                MannerEntity(
                    profile = profileEntity,
                    mannerType = mannerType,
                    count = 1,
                )
            mannerRepository.save(newMannerEntity)
            profileEntity.manners += newMannerEntity
        }

        userEntity.temperature++
        userRepository.save(userEntity)

        profileRepository.save(profileEntity)
    }
}
