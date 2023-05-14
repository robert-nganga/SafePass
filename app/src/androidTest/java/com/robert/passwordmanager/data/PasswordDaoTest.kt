package com.robert.passwordmanager.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.robert.passwordmanager.data.local.PasswordDao
import com.robert.passwordmanager.data.local.PasswordRoomDatabase
import com.robert.passwordmanager.getOrAwaitValue
import com.robert.passwordmanager.models.Account
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PasswordDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var passwordRoomDatabase: PasswordRoomDatabase
    private lateinit var passwordDetails: Account
    private lateinit var passwordDao: PasswordDao

    @Before
    fun setup() {
        passwordRoomDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PasswordRoomDatabase::class.java
        ).allowMainThreadQueries().build()

        passwordDao = passwordRoomDatabase.passwordDao()

        passwordDetails = Account("password", "website",
            "01 020, 2022 04:45", "job@gmail.com", "Google")
    }

    @After
    fun tearDown() {
        passwordRoomDatabase.close()
    }

    @Test
    fun insert() = runTest {
        passwordDao.insert(passwordDetails)

        val allPasswords = passwordDao.getAllPasswords().asLiveData().getOrAwaitValue()
        assertThat(allPasswords).contains(passwordDetails)

    }

    @Test
    fun delete() = runTest {
        passwordDao.insert(passwordDetails)
        passwordDao.delete(passwordDetails = passwordDetails)

        val allPasswords = passwordDao.getAllPasswords().asLiveData().getOrAwaitValue()
        assertThat(allPasswords).doesNotContain(passwordDetails)
    }

    @Test
    fun searchPassword() = runTest {
        passwordDao.insert(passwordDetails)

        val passwords = passwordDao.searchPasswordByName("Google").asLiveData().getOrAwaitValue()
        assertThat(passwords).contains(passwordDetails)
    }
}