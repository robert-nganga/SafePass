package com.robert.passwordmanager.data

import androidx.room.*
import com.robert.passwordmanager.models.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Query("SELECT * FROM password_table ORDER BY id DESC")
    fun getAllPasswords(): Flow<List<Account>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(passwordDetails: Account)

    @Delete
    suspend fun delete(passwordDetails: Account)

    @Query("DELETE FROM password_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM password_table WHERE website_name LIKE :name")
    fun searchPasswordByName(name: String): Flow<List<Account>>
}