package ru.nikolas_snek.peoples.presentation.mvi

import com.example.mvi.MviStore

class PeoplesStore(
	reducer: PeoplesReducer,
	actor: PeoplesActor
) : MviStore<PeoplesPartialState, PeoplesIntent, PeoplesState, PeoplesEffect>(
	reducer,
	actor
) {
	override fun createInitialState(): PeoplesState = PeoplesState()

}