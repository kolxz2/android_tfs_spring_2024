package ru.nikolas_snek.peoples.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUserProfilesUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke() = withContext(Dispatchers.Default) {
        repository.getUserList()
    }
}