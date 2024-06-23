package ru.nikolas_snek.channels.di

import dagger.Component
import ru.nikolas_snek.channels.presentation.all_streams.AllStreamsFragment
import ru.nikolas_snek.data.di.AppSingletons

@Component(
	dependencies = [AppSingletons::class],
	modules = [AllStreamsModule::class, CoroutineScopeModule::class, ChannelsRepositoryModule::class]
)
interface AllStreamsFragmentComponent {
	fun inject(fragment: AllStreamsFragment)

	@Component.Builder
	interface Builder {
		fun deps(articlesDeps: AppSingletons): Builder
		fun build(): AllStreamsFragmentComponent
	}
}