package com.robert.passwordmanager.di

import android.content.Context
import androidx.room.Room
import com.robert.passwordmanager.data.PasswordRoomDatabase
import com.robert.passwordmanager.utils.Contants.PASSWORD_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PasswordModule {

    @Provides
    @Singleton
    fun providesPasswordDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        PasswordRoomDatabase::class.java,
        PASSWORD_DATABASE
    ).build()
}