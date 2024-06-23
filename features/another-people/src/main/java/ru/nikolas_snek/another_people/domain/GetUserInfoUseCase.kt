package ru.nikolas_snek.another_people.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
	private val userInfoRepositoryImpl: UserInfoRepository
) {
	suspend operator fun invoke(userId: Int) = withContext(Dispatchers.IO) {
		userInfoRepositoryImpl.getUserInfo(userId)
	}
}