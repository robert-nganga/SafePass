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
    private val testDispatcher = StandardTestDispatcher()



    @Before
    fun setup(){
        val accounts = mutableListOf(
            Account(websiteName = "Facebook", userName = "johnie@yahoo.com", category = "Application", password = "gdjAgdkd@!AS142@hsAg",
                passwordStrength = 0.8, passwordStrengthLabel = "Very strong password", date = "12 may 2023"),
            Account(websiteName = "Dropbox", userName = "mercy@yahoo.com", category = "Website", password = "gdj237Tads!AS142@hsAg",
                passwordStrength = 0.8, passwordStrengthLabel = "Very strong password", date = "10 may 2023")
        )

        fakeAccountRepository = FakeAccountRepository(accounts)
        val passwordManager = PasswordManager()
        viewModel = AccountViewModel(passwordManager, fakeAccountRepository)
    }

    @Test
    fun `testInsertAccount`() {
        val account = Account(websiteName = "Stripe", userName = "david@gmail.com", category = "Payment", password = "Ps524@31t6Da",
            passwordStrength = 0.7, passwordStrengthLabel = "Strong password", date = "10 may 2023")
        viewModel.insertAccount(account)
        assertThat(fakeAccountRepository.accounts).contains(account)
    }
}