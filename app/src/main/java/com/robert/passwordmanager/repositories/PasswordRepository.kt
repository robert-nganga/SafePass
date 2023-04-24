package com.robert.passwordmanager.repositories

import com.robert.passwordmanager.models.PasswordDetails
import kotlinx.coroutines.flow.Flow

interface PasswordRepository {

    fun searchPasswords(query: String): Flow<List<PasswordDetails>>

    suspend fun insert(passwordDetails: PasswordDetails)

    suspend fun delete(passwordDetails: PasswordDetails)
}