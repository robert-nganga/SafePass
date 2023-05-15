package com.robert.passwordmanager.data.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.robert.passwordmanager.models.Account
import kotlinx.coroutines.flow.Flow

class FakeAccountRepository(private val list: List<Account>): AccountRepository {

    private var accounts = MutableLiveData<List<Account>>()

    init {
        accounts.value = list
    }

    override fun observeAllAccounts(): Flow<List<Account>> {
        return accounts.asFlow()
    }

    override fun searchPasswords(query: String): Flow<List<Account>> {
        return accounts.map { accounts-> accounts.filter { it.websiteName == query } }.asFlow()
    }

    override fun getAccountById(id: Int): Flow<Account> {
        return accounts.map { accounts->
            accounts.first { it.id == id }
        }.asFlow()
    }

    override suspend fun insert(account: Account) {
        TODO("Not yet implemented")
    }

    override suspend fun update(account: Account) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(account: Account) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {

    }
}