package com.example.gieok_moa

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

//@Database(entities = arrayOf(User::class, Snap::class, Tag::class), version = 1)
@Database(
    version = 2,
    entities = arrayOf(User::class, Snap::class, Tag::class),
    autoMigrations = [AutoMigration (from = 1, to = 2)],
    exportSchema = true)
@TypeConverters(
    value = [
        Converters::class,
    ]
)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): userDao
    abstract fun snapDao(): snapDao
    abstract fun tagDao(): tagDao
    companion object {
        private var instance: UserDatabase? = null

        @Synchronized
        fun getInstance(context: Context): UserDatabase? {
            if (instance == null) {
                synchronized(UserDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user-database"
                    ).build()
                }
            }
            return instance
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
