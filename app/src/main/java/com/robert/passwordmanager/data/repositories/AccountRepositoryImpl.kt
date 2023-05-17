package com.robert.passwordmanager.data.repositories

import androidx.lifecycle.LiveData
import com.robert.passwordmanager.data.local.AccountDao
import com.robert.passwordmanager.models.Account
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl@Inject constructor(private val accountDao: AccountDao): AccountRepository{



    override fun observeAllAccounts(): LiveData<List<Account>> = accountDao.getAllPasswords()

    override fun searchPasswords(query: String): LiveData<List<Account>> = accountDao.searchPasswordByName(query)

    override fun getAccountById(id: Int): LiveData<Account> = accountDao.getAccountById(id)

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