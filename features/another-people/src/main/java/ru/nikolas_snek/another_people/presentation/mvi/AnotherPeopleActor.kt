package ru.nikolas_snek.another_people.presentation.mvi

import com.example.mvi.MviActor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import ru.nikolas_snek.another_people.domain.GetUserInfoUseCase
import ru.nikolas_snek.another_people.domain.UserInfoRepository
import ru.nikolas_snek.another_people.domain.UserProfile
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AnotherPeopleActor @Inject constructor(
    private val ownUserRepositoryImpl: UserInfoRepository
) :
    MviActor<AnotherPeoplePartialState, AnotherPeopleIntent, AnotherPeopleState<UserProfile>, AnotherPeopleEffect>() {

    override suspend fun resolve(
        intent: AnotherPeopleIntent,
        state: AnotherPeopleState<UserProfile>
    ): Flow<AnotherPeoplePartialState> {
        return when (intent) {
            is AnotherPeopleIntent.Init -> getUserInfo(intent.userId)
            is AnotherPeopleIntent.Reload -> getUserInfo(intent.userId)
            AnotherPeopleIntent.OnBackClicked -> {
                _effects.emit(AnotherPeopleEffect.NavigateGoBack)
                emptyFlow()
            }
        }
    }

    private fun getUserInfo(userId: Int): Flow<AnotherPeoplePartialState> = flow {
        try {
            val userProfile = GetUserInfoUseCase(ownUserRepositoryImpl).invoke(userId)
            emit(AnotherPeoplePartialState.DataLoaded(userProfile))
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            emit(AnotherPeoplePartialState.Error)
        }
    }

}