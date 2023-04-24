package com.robert.passwordmanager

import android.app.Application
import com.robert.passwordmanager.repositories.PasswordRepository
import com.robert.passwordmanager.room.PasswordRoomDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PasswordsApplication: Application()