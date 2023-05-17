package com.robert.passwordmanager.data.repositories

import androidx.lifecycle.LiveData
import com.robert.passwordmanager.models.Account

interface AccountRepository {

    fun observeAllAccounts(): LiveData<List<Account>>

    fun searchPasswords(query: String): LiveData<List<Account>>

    fun getAccountById(id: Int): LiveData<Account>

    suspend fun insert(account: Account)

    suspend fun update(account: Account)

    suspend fun delete(account: Account)

    suspend fun deleteAll()
}