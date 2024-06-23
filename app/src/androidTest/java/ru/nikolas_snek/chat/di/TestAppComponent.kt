package ru.nikolas_snek.chat.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.nikolas_snek.chat.AppTest
import ru.nikolas_snek.data.di.RepositoryModule
import ru.nikolas_snek.tfsspring2024.di.AppComponent
import ru.nikolas_snek.tfsspring2024.di.AppScreensModule
import ru.nikolas_snek.tfsspring2024.di.CiceroneModule
import ru.nikolas_snek.tfsspring2024.di.DatabaseModule
import javax.inject.Singleton

@Singleton
@Component(modules = [TestRetrofitModule::class, AppScreensModule::class, CiceroneModule::class, RepositoryModule::class, DatabaseModule::class])
interface TestAppComponent : AppComponent {
    fun inject(test: AppTest)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): TestAppComponent
    }
}