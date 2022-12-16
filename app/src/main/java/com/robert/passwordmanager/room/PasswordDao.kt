package com.robert.passwordmanager.room

import androidx.room.*
import com.robert.passwordmanager.models.PasswordDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Query("SELECT * FROM password_table ORDER BY id DESC")
    fun getAllPasswords(): Flow<List<PasswordDetails>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(passwordDetails: PasswordDetails)

    @Delete
    suspend fun delete(passwordDetails: PasswordDetails)


    @Query("SELECT * FROM password_table WHERE website_name LIKE :name")
    fun searchPasswordByName(name: String): Flow<List<PasswordDetails>>
}