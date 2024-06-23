package ru.nikolas_snek.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.nikolas_snek.data.database.models.MessageEntity
import ru.nikolas_snek.data.database.models.OwnUserEntity
import ru.nikolas_snek.data.database.models.ProfileEntity
import ru.nikolas_snek.data.database.models.ReactionEntity
import ru.nikolas_snek.data.database.models.StreamsEntity
import ru.nikolas_snek.data.database.models.TopicEntity

@Database(
    entities = [
        ProfileEntity::class,
        TopicEntity::class,
        StreamsEntity::class,
        MessageEntity::class,
        ReactionEntity::class,
        OwnUserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ZulipDatabase : RoomDatabase() {
    abstract fun userDao(): ZulipDao

}