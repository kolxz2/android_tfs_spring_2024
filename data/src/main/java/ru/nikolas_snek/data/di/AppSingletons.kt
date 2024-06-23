package ru.nikolas_snek.data.di

import ru.nikolas_snek.data.ZulipRemoteSource
import ru.nikolas_snek.data.database.ZulipDao

interface AppSingletons {

    val zulipRemoteSource : ZulipRemoteSource

    val zulipDao : ZulipDao
}