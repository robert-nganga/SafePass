package com.robert.passwordmanager.data.repositories

import com.robert.passwordmanager.models.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    fun observeAllAccounts(): Flow<List<Account>>

    fun searchPasswords(query: String): Flow<List<Account>>

    fun getAccountById(id: Int): Flow<Account>

    suspend fun insert(account: Account)

    suspend fun update(account: Account)

    suspend fun delete(account: Account)

    suspend fun deleteAll()
}