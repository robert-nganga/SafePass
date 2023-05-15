package com.robert.passwordmanager.data.local

import androidx.room.*
import com.robert.passwordmanager.models.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM password_table ORDER BY id DESC")
    fun getAllPasswords(): Flow<List<Account>>

    @Query("SELECT * FROM password_table where id = :id")
    fun getAccountById(id: Int): Flow<Account>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: Account)

    @Update
    suspend fun update(account: Account)

    @Delete
    suspend fun delete(passwordDetails: Account)

    @Query("DELETE FROM password_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM password_table WHERE website_name LIKE :name")
    fun searchPasswordByName(name: String): Flow<List<Account>>
}