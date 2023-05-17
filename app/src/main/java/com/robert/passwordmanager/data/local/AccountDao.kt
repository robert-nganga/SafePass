package com.robert.passwordmanager.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.robert.passwordmanager.models.Account

@Dao
interface AccountDao {

    @Query("SELECT * FROM password_table ORDER BY id DESC")
    fun getAllPasswords(): LiveData<List<Account>>

    @Query("SELECT * FROM password_table where id = :id")
    fun getAccountById(id: Int): LiveData<Account>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: Account)

    @Update
    suspend fun update(account: Account)

    @Delete
    suspend fun delete(passwordDetails: Account)

    @Query("DELETE FROM password_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM password_table WHERE website_name LIKE :name")
    fun searchPasswordByName(name: String): LiveData<List<Account>>
}