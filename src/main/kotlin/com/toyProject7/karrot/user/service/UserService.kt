package com.toyProject7.karrot.user.service

import com.toyProject7.karrot.profile.persistence.ProfileEntity
import com.toyProject7.karrot.profile.service.ProfileService
import com.toyProject7.karrot.user.AuthenticateException
import com.toyProject7.karrot.user.SignInInvalidPasswordException
import com.toyProject7.karrot.user.SignInUserNotFoundException
import com.toyProject7.karrot.user.SignUpBadNicknameException
import com.toyProject7.karrot.user.SignUpBadPasswordException
import com.toyProject7.karrot.user.SignUpBadUserIdException
import com.toyProject7.karrot.user.SignUpInvalidEmailException
import com.toyProject7.karrot.user.SignUpNicknameConflictException
import com.toyProject7.karrot.user.SignUpUserIdConflictException
import com.toyProject7.karrot.user.UserAccessTokenUtil
import com.toyProject7.karrot.user.UserNotFoundException
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.persistence.NormalUser
import com.toyProject7.karrot.user.persistence.SocialUser
import com.toyProject7.karrot.user.persistence.UserEntity
import com.toyProject7.karrot.user.persistence.UserPrincipal
import com.toyProject7.karrot.user.persistence.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class UserService(
    private val userRepository: UserRepository,
    @Lazy private val profileService: ProfileService,
) {
    @Transactional
    fun signUp(
        nickname: String,
        userId: String,
        password: String,
        email: String,
    ): User {
        if (userId.length < 5 || userId.length > 20) {
            throw SignUpBadUserIdException()
        }
        if (password.length < 8 || password.length > 16) {
            throw SignUpBadPasswordException()
        }
        if (nickname.length < 2 || nickname.length > 10) {
            throw SignUpBadNicknameException()
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(email)) {
            throw SignUpInvalidEmailException()
        }

        if (userRepository.existsByUserId(userId)) {
            throw SignUpUserIdConflictException()
        }
        if (userRepository.existsByNickname(nickname)) {
            throw SignUpNicknameConflictException()
        }
        val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val user =
            userRepository.save(
                NormalUser(
                    nickname = nickname,
                    userId = userId,
                    hashedPassword = encryptedPassword,
                    location = "void",
                    temperature = 36.5,
                    email = email,
                    imageUrl = null,
                    updatedAt = Instant.now(),
                ),
            )

        val profileEntity =
            ProfileEntity(
                user = user,
            )
        profileService.saveProfileEntity(profileEntity)

        return User.fromEntity(user)
    }

    @Transactional
    fun signIn(
        userId: String,
        password: String,
    ): Pair<User, String> {
        val targetUser = userRepository.findNormalUserByUserId(userId) ?: throw SignInUserNotFoundException()
        if (!BCrypt.checkpw(password, targetUser.hashedPassword)) {
            throw SignInInvalidPasswordException()
        }
        val accessToken = UserAccessTokenUtil.generateAccessToken(targetUser.id!!)
        return Pair(User.fromEntity(targetUser), accessToken)
    }

    @Transactional
    fun authenticate(accessToken: String): User {
        val id = UserAccessTokenUtil.validateAccessTokenGetUserId(accessToken) ?: throw AuthenticateException()
        val user = userRepository.findByIdOrNull(id) ?: throw AuthenticateException()
        return User.fromEntity(user)
    }

    @Transactional
    fun createOrRetrieveSocialUser(
        email: String,
        providerId: String,
        provider: String,
    ): User {
        // Check if the user exists by email
        val existingUser = userRepository.findSocialUserByEmail(email)

        return existingUser?.let {
            // If the user exists, convert to User DTO and return
            User.fromEntity(it)
        } ?: run {
            // If the user doesn't exist, generate a unique random username
            var username = generateRandomString()
            var isUnique = false

            while (!isUnique) {
                if (!userRepository.existsByNickname(username)) {
                    isUnique = true
                }
                username = generateRandomString()
            }

            // Create a new SocialUser with the generated username
            val newUser =
                SocialUser(
                    email = email,
                    nickname = username,
                    provider = provider,
                    providerId = providerId,
                    location = "void",
                    temperature = 36.5,
                    imageUrl = null,
                    updatedAt = Instant.now(),
                )

            // Save the new user
            val savedUser = userRepository.save(newUser)

            // Create and save the associated profile
            val profileEntity =
                ProfileEntity(
                    user = newUser,
                )
            profileService.saveProfileEntity(profileEntity)

            // Convert and return as User DTO
            User.fromEntity(savedUser)
        }
    }

    private fun generateRandomString(length: Int = 8): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { characters.random() }
            .joinToString("")
    }

    @Transactional
    fun getUserEntityById(id: String): UserEntity {
        val userEntity = userRepository.findByIdOrNull(id) ?: throw UserNotFoundException()
        profileService.refreshPresignedUrlIfExpired(userEntity)
        return userEntity
    }

    @Transactional
    fun loadUserPrincipalById(id: String): UserPrincipal {
        val user =
            userRepository.findById(id)
                .orElseThrow { UsernameNotFoundException("User not found with id: $id") }
        return UserPrincipal.create(user)
    }

    @Transactional
    fun getUserEntityByNickname(nickname: String): UserEntity {
        return userRepository.findByNickname(nickname) ?: throw UserNotFoundException()
    }

    @Transactional
    fun existUserEntityByNickname(nickname: String): Boolean {
        return userRepository.existsByNickname(nickname)
    }
}
