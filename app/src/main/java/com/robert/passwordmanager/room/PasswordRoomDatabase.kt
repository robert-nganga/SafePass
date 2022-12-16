package com.robert.passwordmanager.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.robert.passwordmanager.models.PasswordDetails

@Database(entities = [PasswordDetails::class], version = 1, exportSchema = false)
abstract class PasswordRoomDatabase: RoomDatabase() {

    abstract fun passwordDao(): PasswordDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PasswordRoomDatabase? = null

        fun getDatabase(context: Context): PasswordRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PasswordRoomDatabase::class.java,
                    "password_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}