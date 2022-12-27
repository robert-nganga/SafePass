package com.robert.passwordmanager

import androidx.annotation.WorkerThread
import com.robert.passwordmanager.models.PasswordDetails
import com.robert.passwordmanager.room.PasswordDao
import kotlinx.coroutines.flow.Flow

class PasswordRepository(private val passwordDao: PasswordDao){

    val allPasswords: Flow<List<PasswordDetails>> = passwordDao.getAllPasswords()

    fun searchPasswords(name: String): Flow<List<PasswordDetails>> = passwordDao.searchPasswordByName(name)


//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun getCategoryPasswords(name: String): List<PasswordDetails>{
//        return passwordDao.searchPasswordByName(n)
//    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(passwordDetails: PasswordDetails) {
        passwordDao.insert(passwordDetails)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(passwordDetails: PasswordDetails) {
        passwordDao.delete(passwordDetails)
    }

}