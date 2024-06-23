package ru.nikolas_snek.data_di

import ru.nikolas_snek.data.di.AppSingletons
import kotlin.properties.Delegates.notNull

interface AppDepsProvider {
    val appDependencies: AppSingletons

    companion object : AppDepsProvider by AppDepsStore
}

object AppDepsStore : AppDepsProvider {

    override var appDependencies: AppSingletons by notNull()
}
