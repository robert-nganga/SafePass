package com.robert.passwordmanager.ui

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.models.AccountListItem
import com.robert.passwordmanager.repositories.PasswordRepositoryImpl
import com.robert.passwordmanager.utils.OrderBy
import com.robert.passwordmanager.utils.PasswordManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val passwordManager: PasswordManager,
    private val repository: PasswordRepositoryImpl): ViewModel() {

    private var _id = MutableLiveData<Int>()

    val account = _id.switchMap { id ->
        repository.getAccountById(id).asLiveData()
    }

    val allAccounts: LiveData<List<Account>> = repository.allAccounts.asLiveData()

    private var _orderBy = MutableLiveData<OrderBy>(OrderBy.Category)

    val allAccountItems = MediatorLiveData<List<AccountListItem>>().apply {
        addSource(_orderBy){ order ->
            value = allAccounts.value?.let { accounts -> getAccountListItems(accounts, order) }
        }
        addSource(allAccounts){ accounts ->
            value = _orderBy.value?.let { order -> getAccountListItems(accounts, order) }
        }
    }

    fun searchPasswords(name: String): Flow<List<Account>> = repository.searchPasswords(name)

    private val _generatedPassword = MutableLiveData<String>()
    val  generatedPassword: LiveData<String>
        get() = _generatedPassword


    fun generatePassword(isWithLetters: Boolean,
                         isWithNumbers: Boolean,
                         isWithSpecial: Boolean,
                         length: Int){
        _generatedPassword.value = passwordManager.generatePassword(isWithLetters,
             isWithLetters, isWithNumbers, isWithSpecial, length)
    }

    @SuppressLint("SimpleDateFormat")
    fun createAccount(name: String, username: String, category: String, password: String){
        val sdf = SimpleDateFormat("dd MMM, yyy")
        val currentDate: String = sdf.format(Date())
        val account = Account(
            websiteName = name,
            userName = username,
            category = category,
            password = password,
            date = currentDate
        )
        insertAccount(account)
    }

    fun setOrderBY(newOrder: OrderBy){
        _orderBy.value = newOrder
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

    fun getSizeOfEachCategory(categories: Array<String>, items: List<Account> ): Map<String, Int>{
        val sizeMap = mutableMapOf<String, Int>()
        categories.forEach { category ->
            var size = 0
            items.forEach { item ->
                if(category == item.category){
                    size++
                }
            }
            sizeMap[category] = size
        }
        return sizeMap
    }

    private fun getAccountListItems(items: List<Account>, orderBy: OrderBy): List<AccountListItem> {
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
            } } }
    }

    fun validatePassword(password: String): String?{
        return when {
            password.length < 8 -> {"Must be more than 8 characters"}
            !password.matches(".*[A-Z].*".toRegex()) -> {"Must contain Upper-case character"}
            !password.matches(".*[a-z].*".toRegex()) -> {"Must contain Lower-case character"}
            !password.matches(".*[0-9].*".toRegex()) -> {"Must contain a number"}
            !password.matches(".*[!@$#^%*()_><.,:;|}{~`+=].*".toRegex()) -> {"Must contain special character"}
            else->{null}
        }
    }



}