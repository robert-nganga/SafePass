package com.robert.passwordmanager.repositories

import androidx.annotation.WorkerThread
import com.robert.passwordmanager.models.PasswordDetails
import com.robert.passwordmanager.data.PasswordDao
import com.robert.passwordmanager.data.PasswordRoomDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PasswordRepositoryImpl@Inject constructor(database: PasswordRoomDatabase): PasswordRepository{

    private val passwordDao = database.passwordDao()

    val allPasswords: Flow<List<PasswordDetails>> = passwordDao.getAllPasswords()

    override fun searchPasswords(query: String): Flow<List<PasswordDetails>> = passwordDao.searchPasswordByName(query)

    override suspend fun insert(passwordDetails: PasswordDetails) {
        passwordDao.insert(passwordDetails)
    }

    override suspend fun delete(passwordDetails: PasswordDetails) {
        passwordDao.delete(passwordDetails)
    }

}