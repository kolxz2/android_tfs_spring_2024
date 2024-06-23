package ru.nikolas_snek.profile.presentation.mvi

import com.example.mvi.MviActor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nikolas_snek.profile.domain.GetUserInfoUseCase
import ru.nikolas_snek.profile.domain.OwnUserRepository
import ru.nikolas_snek.profile.domain.UserProfile
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ProfileActor @Inject constructor(
    private val ownUserRepositoryImpl: OwnUserRepository
) :
    MviActor<ProfilePartialState, ProfileIntent, ProfileState<UserProfile>, ProfileEffect>() {
    override suspend fun resolve(
        intent: ProfileIntent,
        state: ProfileState<UserProfile>
    ): Flow<ProfilePartialState> {
        return when (intent) {
            ProfileIntent.Init -> getUserInfo()
            ProfileIntent.Reload -> getUserInfo()
        }
    }

    private fun getUserInfo(): Flow<ProfilePartialState> = flow {
        try {
            val userProfile = GetUserInfoUseCase(ownUserRepositoryImpl).invoke()
            emit(ProfilePartialState.DataLoaded(userProfile))
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            emit(ProfilePartialState.Error)
        }
    }
}