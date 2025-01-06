package com.toyProject7.karrot.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.Table

@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Use single table inheritance
@DiscriminatorColumn(name = "user_type") // Column to differentiate user types
@Table(name = "users") // Table name in the database
@Entity(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(name = "nickname", nullable = false)
    var nickname: String,
    @Column(name = "location")
    var location: String,
    @Column(name = "temperature")
    var temperature: Double,
    @Column(name = "email")
    var email: String,
)
