package com.toyProject7.karrot.user.persistence

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserPrincipal(
    val id: String,
    private val email: String,
    private val nickname: String,
    private val password: String?,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    companion object {
        fun create(user: UserEntity): UserPrincipal {
            val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))

            return UserPrincipal(
                id = user.id!!,
                email = user.email,
                nickname = user.nickname,
                password = null, // Password can be null for social login
                authorities = authorities
            )
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities
    override fun getPassword(): String? = password
    fun getNickname(): String = nickname
    override fun getUsername(): String = email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}