package com.robert.passwordmanager.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.robert.passwordmanager.models.PasswordDetails

@Database(entities = [PasswordDetails::class], version = 1, exportSchema = false)
abstract class PasswordRoomDatabase: RoomDatabase() {

    abstract fun passwordDao(): PasswordDao
}