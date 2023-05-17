package com.robert.passwordmanager.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.robert.passwordmanager.models.Account
import kotlinx.coroutines.runBlocking

class FakeAccountRepository(): AccountRepository {

    private var accounts = mutableListOf<Account>()
    private var observableAccounts = MutableLiveData<List<Account>>()

    init {
        accounts.add(Account(websiteName = "Facebook", userName = "johnie@yahoo.com", category = "Application", password = "gdjAgdkd@!AS142@hsAg",
            passwordStrength = 0.8, passwordStrengthLabel = "Very strong password", date = "12 may 2023"))
        accounts.add(Account(websiteName = "Dropbox", userName = "mercy@yahoo.com", category = "Website", password = "gdj237Tads!AS142@hsAg",
            passwordStrength = 0.8, passwordStrengthLabel = "Very strong password", date = "10 may 2023"))
        observableAccounts.postValue(accounts)

        runBlocking { check() }
    }



    override fun observeAllAccounts() = observableAccounts

    override fun searchPasswords(query: String) = observableAccounts.map { list->
        list.filter { it.websiteName == query }
    }

    override fun getAccountById(id: Int) = observableAccounts.map { list->
        list.first { it.id == id }
    }

    override suspend fun insert(account: Account) {
        accounts.add(account)
        observableAccounts.postValue(accounts)
    }

    override suspend fun update(account: Account) {
        accounts.removeIf { it.id == account.id }
        accounts.add(account)
        observableAccounts.postValue(accounts)
    }

    override suspend fun delete(account: Account) {
        accounts.remove(account)
        observableAccounts.postValue(accounts)
    }

    override suspend fun deleteAll() {
        accounts.clear()
        observableAccounts.postValue(accounts)
    }

    suspend fun check(){
        val account = Account(websiteName = "Stripe", userName = "david@gmail.com", category = "Payment", password = "Ps524@31t6Da",
            passwordStrength = 0.7, passwordStrengthLabel = "Strong password", date = "10 may 2023")
        insert(account)
        val list = observableAccounts.value
        println(list)
    }

}