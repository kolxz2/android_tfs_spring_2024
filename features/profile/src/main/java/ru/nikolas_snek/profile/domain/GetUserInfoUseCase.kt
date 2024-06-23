package ru.nikolas_snek.profile.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUserInfoUseCase(
    private val ownUserRepository: OwnUserRepository
) {
    suspend operator fun invoke(): UserProfile = withContext(Dispatchers.Default) {
        ownUserRepository.getUserProfile()
    }
}