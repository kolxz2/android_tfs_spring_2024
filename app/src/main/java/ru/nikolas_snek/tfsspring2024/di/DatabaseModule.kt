package ru.nikolas_snek.tfsspring2024.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.nikolas_snek.data.database.ZulipDao
import ru.nikolas_snek.data.database.ZulipDatabase
import javax.inject.Singleton


@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): ZulipDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ZulipDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: ZulipDatabase): ZulipDao {
        return appDatabase.userDao()
    }
}
