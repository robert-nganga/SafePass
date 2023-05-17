package com.robert.passwordmanager.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.robert.passwordmanager.data.repositories.FakeAccountRepository
import com.robert.passwordmanager.getOrAwaitValueTest
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.utils.PasswordManager
import com.google.common.truth.Truth.assertThat
import com.robert.passwordmanager.MainCoroutineRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


@OptIn(ExperimentalCoroutinesApi::class)
class AccountViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModel: AccountViewModel
    private lateinit var fakeAccountRepository: FakeAccountRepository



    @Before
    fun setup(){
        fakeAccountRepository = FakeAccountRepository()
        val passwordManager = PasswordManager()
        viewModel = AccountViewModel(passwordManager, fakeAccountRepository)
    }

    @Test
    fun testInsertAccount() {
        val account = Account(websiteName = "Stripe", userName = "david@gmail.com", category = "Payment", password = "Ps524@31t6Da",
            passwordStrength = 0.7, passwordStrengthLabel = "Strong password", date = "10 may 2023")
        viewModel.insertAccount(account)
        val result = viewModel.allAccounts.getOrAwaitValueTest()
        assertThat(result).contains(account)
    }
}