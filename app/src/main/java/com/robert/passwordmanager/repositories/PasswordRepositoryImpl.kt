package com.robert.passwordmanager.repositories

import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.data.PasswordRoomDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PasswordRepositoryImpl@Inject constructor(database: PasswordRoomDatabase): PasswordRepository{

    private val accountDao = database.passwordDao()

    val allAccounts: Flow<List<Account>> = accountDao.getAllPasswords()

    override fun searchPasswords(query: String): Flow<List<Account>> = accountDao.searchPasswordByName(query)

    override fun getAccountById(id: Int): Flow<Account> = accountDao.getAccountById(id)

    override suspend fun insert(account: Account) {
        accountDao.insert(account)
    }

    override suspend fun update(account: Account) {
        accountDao.update(account)
    }

    override suspend fun delete(account: Account) {
        accountDao.delete(account)
    }

    override suspend fun deleteAll() {
        accountDao.deleteAll()
    }


}