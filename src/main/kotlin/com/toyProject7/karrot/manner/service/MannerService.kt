package com.toyProject7.karrot.manner.service

import com.toyProject7.karrot.manner.controller.MannerType
import com.toyProject7.karrot.manner.persistence.MannerEntity
import com.toyProject7.karrot.manner.persistence.MannerRepository
import com.toyProject7.karrot.profile.controller.Profile
import com.toyProject7.karrot.profile.service.ProfileService
import com.toyProject7.karrot.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MannerService(
    private val mannerRepository: MannerRepository,
    private val userService: UserService,
    private val profileService: ProfileService,
) {
    @Transactional
    fun increaseMannerCount(
        nickname: String,
        mannerType: MannerType,
    ) {
        val userEntity = userService.getUserEntityByNickname(nickname)
        val profileEntity = profileService.getProfileEntityByUserId(userEntity.id!!)
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
    }
}
