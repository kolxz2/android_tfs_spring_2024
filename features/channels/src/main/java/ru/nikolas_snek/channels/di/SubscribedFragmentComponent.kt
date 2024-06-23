package ru.nikolas_snek.channels.di

import dagger.Component
import ru.nikolas_snek.channels.presentation.subscribed.SubscribedFragment
import ru.nikolas_snek.data.di.AppSingletons

@Component(
	dependencies = [AppSingletons::class],
	modules = [SubscribedModule::class, CoroutineScopeModule::class, ChannelsRepositoryModule::class]
)
interface SubscribedFragmentComponent {
	fun inject(channelsFragment: SubscribedFragment)

	@Component.Builder
	interface Builder {
		fun deps(articlesDeps: AppSingletons): Builder

		fun build(): SubscribedFragmentComponent
	}
}