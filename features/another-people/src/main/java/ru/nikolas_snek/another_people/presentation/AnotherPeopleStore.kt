package ru.nikolas_snek.another_people.presentation

import com.example.mvi.MviStore
import ru.nikolas_snek.another_people.domain.UserProfile
import ru.nikolas_snek.another_people.presentation.mvi.AnotherPeopleActor
import ru.nikolas_snek.another_people.presentation.mvi.AnotherPeopleEffect
import ru.nikolas_snek.another_people.presentation.mvi.AnotherPeopleIntent
import ru.nikolas_snek.another_people.presentation.mvi.AnotherPeoplePartialState
import ru.nikolas_snek.another_people.presentation.mvi.AnotherPeopleReducer
import ru.nikolas_snek.another_people.presentation.mvi.AnotherPeopleState

class AnotherPeopleStore(
    reducer: AnotherPeopleReducer,
    actor: AnotherPeopleActor
) : MviStore<AnotherPeoplePartialState, AnotherPeopleIntent, AnotherPeopleState<UserProfile>, AnotherPeopleEffect>(
    reducer,
    actor
) {
    override fun createInitialState(): AnotherPeopleState<UserProfile> = AnotherPeopleState.Loading
}