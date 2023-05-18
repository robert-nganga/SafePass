package com.robert.passwordmanager.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.robert.passwordmanager.data.repositories.AccountRepository
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.models.AccountListItem
import com.robert.passwordmanager.utils.OrderBy
import com.robert.passwordmanager.utils.PasswordManager
import com.robert.passwordmanager.utils.Report
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val passwordManager: PasswordManager,
    private val repository: AccountRepository
): ViewModel() {

    private var _id = MutableLiveData<Int>()

    private var _searchQuery = MutableLiveData<String>()

    val searchResults =  _searchQuery.switchMap { query->
        Log.i("Search", "search livedata updated query:: $query")
        repository.searchPasswords(query)
    }

    val account = _id.switchMap { id ->
        repository.getAccountById(id)
    }

    val allAccounts: LiveData<List<Account>> = repository.observeAllAccounts()

    val averagePasswordStrength = allAccounts.map { accounts->
        getAverage(accounts)
    }

    val report = allAccounts.map { accounts->
        getReport(accounts)
    }

    private var _orderBy = MutableLiveData<OrderBy>(OrderBy.Category)

    val allAccountItems = MediatorLiveData<List<AccountListItem>>().apply {
        addSource(_orderBy){ order ->
            value = allAccounts.value?.let { accounts -> getAccountListItems(accounts, order) }
        }
        addSource(allAccounts){ accounts ->
            value = _orderBy.value?.let { order -> getAccountListItems(accounts, order) }
        }
    }

    private val _generatedPassword = MutableLiveData<String>()
    val  generatedPassword: LiveData<String>
        get() = _generatedPassword


    fun setOrderBY(newOrder: OrderBy){
        _orderBy.value = newOrder
    }

    fun setSearchQuery(newQuery: String){
        _searchQuery.value = newQuery
    }

    fun setId(newId: Int){
        _id.value = newId
    }

    fun insertAccount(account: Account) = viewModelScope.launch{
        repository.insert(account)
    }

    fun updateAccount(account: Account) = viewModelScope.launch {
        repository.update(account)
    }

    fun deleteAccount(passwordDetails: Account) = viewModelScope.launch{
        repository.delete(passwordDetails)
    }

    fun evaluatePassword(password: String): Float {
        return passwordManager.evaluatePassword(password)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun getPasswordStrengthLabel(strength: Float): String{
        return  when (strength) {
            in 0.0 .. 0.55 -> {
                "Weak password"
            }
            in 0.55 .. 0.75 -> {
                "Strong password"
            }
            in 0.75 .. 1.0 -> {
                "Very strong password"
            }
            else -> {""}
        }
    }

    fun generatePassword(isWithLetters: Boolean,
                         isWithNumbers: Boolean,
                         isWithSpecial: Boolean,
                         length: Int){
        _generatedPassword.value = passwordManager.generatePassword(isWithLetters,
            isWithLetters, isWithNumbers, isWithSpecial, length)
    }

    @SuppressLint("SimpleDateFormat")
    fun createAccount(name: String, username: String, category: String, password: String): Account {
        val sdf = SimpleDateFormat("dd MMM, yyy")
        val currentDate: String = sdf.format(Date())
        return Account(
            websiteName = name,
            userName = username,
            category = category,
            password = password,
            date = currentDate,
            passwordStrength = evaluatePassword(password).toDouble(),
            passwordStrengthLabel = getPasswordStrengthLabel(evaluatePassword(password))
        )
    }

    fun getAverage(accounts: List<Account>): Float {
        val strengthSum = accounts.sumOf { it.passwordStrength }
        return (strengthSum / accounts.size).toFloat()
    }

    fun getReport(accounts: List<Account>): Report {
        return Report(
            total = accounts.size,
            safe = accounts.filter { it.passwordStrength > 0.55 }.size,
            weak = accounts.filter { it.passwordStrengthLabel == "Weak password" }.size,
            reused = accounts.groupBy { it.password }.values.count { it.size > 1 }
        )
    }

    fun getAccountListItems(items: List<Account>, orderBy: OrderBy): List<AccountListItem> {
        if (items.isEmpty()) return emptyList()

        val accountHeaders = items
            .groupBy {
                when (orderBy){
                    is OrderBy.Category -> {it.category}
                    is OrderBy.Date -> {it.date}
                }
            }.map { (category, passwords) ->
                val sum = passwords.sumOf { it.id }
                AccountListItem.AccountHeaderItem(category, sum)
            }
        val accountItems = items.map { AccountListItem.AccountItem(it) }

        return accountHeaders.flatMap { listOf(it) + accountItems.filter { item ->
            when (orderBy){
                is OrderBy.Date -> { item.account.date == it.title }
                is OrderBy.Category -> { item.account.category == it.title }
            }
        }
        }
    }
}