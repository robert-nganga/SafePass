package com.robert.passwordmanager

import android.app.Application
import com.robert.passwordmanager.repositories.PasswordRepository
import com.robert.passwordmanager.room.PasswordRoomDatabase

class PasswordsApplication: Application() {

    val database by lazy { PasswordRoomDatabase.getDatabase(this) }
    val repository by lazy { PasswordRepository(database.passwordDao()) }
}