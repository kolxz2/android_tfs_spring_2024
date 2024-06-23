package ru.nikolas_snek.chat.di

import dagger.Component
import ru.nikolas_snek.chat.presentation.ChatFragment
import ru.nikolas_snek.data.di.AppSingletons
import ru.nikolas_snek.data.di.RepositoryModule

@Component(
    dependencies = [AppSingletons::class],
    modules = [ChatModule::class, CoroutineChatModule::class])
interface ChatFragmentComponent {
    fun inject(fragment: ChatFragment)

    @Component.Builder
    interface Builder {
        fun deps(articlesDeps: AppSingletons): Builder

        fun build(): ChatFragmentComponent
    }
}