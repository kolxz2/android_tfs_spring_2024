package ru.nikolas_snek.profile.di

import dagger.Component
import ru.nikolas_snek.data.di.AppSingletons
import ru.nikolas_snek.profile.presentation.ProfileFragment

@Component(
    dependencies = [AppSingletons::class],
    modules = [ProfileStoreFactoryModule::class, OwnUserRepositoryModule::class]
)
interface ProfileComponent {
    fun inject(fragment: ProfileFragment)

    @Component.Builder
    interface Builder {
        fun deps(articlesDeps: AppSingletons): Builder
        fun build(): ProfileComponent
    }
}