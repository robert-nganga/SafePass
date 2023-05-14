package com.robert.passwordmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.robert.passwordmanager.models.Account

@Database(entities = [Account::class], version = 1, exportSchema = false)
abstract class PasswordRoomDatabase: RoomDatabase() {

    abstract fun passwordDao(): PasswordDao
}