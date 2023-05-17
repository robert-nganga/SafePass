package com.robert.passwordmanager.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.robert.passwordmanager.data.repositories.FakeAccountRepository
import com.robert.passwordmanager.getOrAwaitValueTest
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.utils.PasswordManager
import com.google.common.truth.Truth.assertThat
import com.robert.passwordmanager.MainCoroutineRule
import com.robert.passwordmanager.utils.OrderBy
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
    fun createAccount_validInputs_returnsCorrectValues(){
        val res = viewModel.createAccount(
            name = "Dropbox",
            username = "mercy@yahoo.com",
            category = "Website",
            password = "gdj237Tads!AS142@hsAg"
        )

        assertThat(res.date).isEqualTo("17 May, 2023")
        assertThat(res.passwordStrength).isGreaterThan(0.8)
        assertThat(res.passwordStrengthLabel).isEqualTo("Very strong password")
    }

    @Test
    fun getAverage_givenValidList_returnsCorrectAverage(){
        val list = listOf(
            Account(websiteName = "Dropbox", userName = "mercy@yahoo.com", category = "Website", password = "gdj237Tads!AS142@hsAg",
                passwordStrength = 0.9, passwordStrengthLabel = "Very strong password", date = "10 may 2023"),
            Account(websiteName = "Facebook", userName = "johnie@yahoo.com", category = "Application", password = "gdjAgdkd@!AS142@hsAg",
                passwordStrength = 0.8, passwordStrengthLabel = "Very strong password", date = "12 may 2023")
        )
        val res = viewModel.getAverage(list)
        assertThat(res).isEqualTo(0.85f)
    }

    @Test
    fun getReport_givenValidList_returnsCorrectReport(){
        val list = listOf(
            Account(websiteName = "Dropbox", userName = "mercy@yahoo.com", category = "Website", password = "gdj237Tads!AS142@hsAg",
                passwordStrength = 0.9, passwordStrengthLabel = "Very strong password", date = "10 may 2023"),
            Account(websiteName = "Facebook", userName = "johnie@yahoo.com", category = "Application", password = "gdjAgdkd@!AS142@hsAg",
                passwordStrength = 0.8, passwordStrengthLabel = "Very strong password", date = "12 may 2023")
        )
        val report = viewModel.getReport(list)
        assertThat(report.total).isEqualTo(2)
        assertThat(report.weak).isEqualTo(0)
        assertThat(report.reused).isEqualTo(0)
        assertThat(report.strong).isEqualTo(2)
    }

    @Test
    fun getAccountItems_givenValidInput_returnsCorrectList(){
        val list = listOf(
            Account(websiteName = "Dropbox", userName = "mercy@yahoo.com", category = "Website", password = "gdj237Tads!AS142@hsAg",
                passwordStrength = 0.9, passwordStrengthLabel = "Very strong password", date = "10 may 2023"),
            Account(websiteName = "Facebook", userName = "johnie@yahoo.com", category = "Application", password = "gdjAgdkd@!AS142@hsAg",
                passwordStrength = 0.8, passwordStrengthLabel = "Very strong password", date = "12 may 2023")
        )
        val items = viewModel.getAccountListItems(list, OrderBy.Category)

        assertThat(items.size).isEqualTo(4)
    }

    @Test
    fun testInsert(){
        val acc = Account(websiteName = "Stripe", id = 2, userName = "david@gmail.com", category = "Payment", password = "Ps524@31t6Da",
            passwordStrength = 0.7, passwordStrengthLabel = "Strong password", date = "10 may 2023")

        viewModel.insertAccount(acc)
        val result = viewModel.allAccounts.getOrAwaitValueTest()
        assertThat(result).contains(acc)
    }
}