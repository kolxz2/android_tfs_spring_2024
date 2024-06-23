package ru.nikolas_snek.another_people.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.nikolas_snek.another_people.presentation.AnotherPeopleStore
import javax.inject.Inject

class AnotherPeopleStoreFactory @Inject constructor(
    private val reducer: AnotherPeopleReducer,
    private val actor: AnotherPeopleActor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AnotherPeopleStore(reducer, actor) as T
    }
}