package ru.nikolas_snek.peoples.di

import dagger.Component
import ru.nikolas_snek.data.di.AppSingletons
import ru.nikolas_snek.peoples.presentation.PeoplesFragment

@Component(
    dependencies = [AppSingletons::class],
    modules = [ProfileModule::class, CoroutineModule::class]
)
interface PeoplesFragmentComponent {
    fun inject(fragment: PeoplesFragment)

    @Component.Builder
    interface Builder {
        fun deps(articlesDeps: AppSingletons): Builder

        fun build(): PeoplesFragmentComponent
    }
}