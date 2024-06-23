package ru.nikolas_snek.another_people.di

import dagger.Component
import ru.nikolas_snek.another_people.presentation.AnotherPeopleFragment
import ru.nikolas_snek.data.di.AppSingletons

@Component(
	dependencies = [AppSingletons::class],
	modules = [UserInfoRepositoryModule::class]
)
interface AnotherPeopleFragmentComponent {
	fun inject(anotherPeopleFragment: AnotherPeopleFragment)

	@Component.Builder
	interface Builder {
		fun deps(articlesDeps: AppSingletons): Builder

		fun build(): AnotherPeopleFragmentComponent
	}
}