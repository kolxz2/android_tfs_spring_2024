package ru.nikolas_snek.another_people.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.nikolas_snek.another_people.data.UserInfoRepositoryImpl
import ru.nikolas_snek.another_people.domain.UserInfoRepository
import ru.nikolas_snek.another_people.presentation.mvi.AnotherPeopleStoreFactory

@Module
interface UserInfoRepositoryModule {
    @Binds
    fun bindUserInfoRepository(userInfoRepository: UserInfoRepositoryImpl): UserInfoRepository

    @Binds
    fun bindViewModelFactory(profileStoreFactory: AnotherPeopleStoreFactory): ViewModelProvider.Factory
}