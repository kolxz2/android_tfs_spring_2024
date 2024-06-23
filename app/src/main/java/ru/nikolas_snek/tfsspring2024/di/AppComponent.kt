package ru.nikolas_snek.tfsspring2024.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.nikolas_snek.data.ZulipRemoteSource
import ru.nikolas_snek.data.database.ZulipDao
import ru.nikolas_snek.data.di.AppSingletons
import ru.nikolas_snek.data.di.RepositoryModule
import ru.nikolas_snek.tfsspring2024.App
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppScreensModule::class,
        CiceroneModule::class,
        RepositoryModule::class,
        RetrofitModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent : AppSingletons {
    fun inject(app: App)

    override val zulipRemoteSource: ZulipRemoteSource

    override val zulipDao : ZulipDao

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance  context: Context): AppComponent
    }
}