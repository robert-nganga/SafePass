package com.robert.passwordmanager.repositories

import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.data.PasswordRoomDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PasswordRepositoryImpl@Inject constructor(database: PasswordRoomDatabase): PasswordRepository{

    private val passwordDao = database.passwordDao()

    val allPasswords: Flow<List<Account>> = passwordDao.getAllPasswords()

    override fun searchPasswords(query: String): Flow<List<Account>> = passwordDao.searchPasswordByName(query)

    override fun getAccountById(id: Int): Flow<Account> = passwordDao.getAccountById(id)

    override suspend fun upsert(passwordDetails: Account) {
        passwordDao.upsert(passwordDetails)
    }

    override suspend fun delete(passwordDetails: Account) {
        passwordDao.delete(passwordDetails)
    }

    override suspend fun deleteAll() {
        passwordDao.deleteAll()
    }


}