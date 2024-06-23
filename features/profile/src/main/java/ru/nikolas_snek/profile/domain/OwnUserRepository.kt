package ru.nikolas_snek.profile.domain

interface OwnUserRepository {
    suspend fun getUserProfile(): UserProfile
}