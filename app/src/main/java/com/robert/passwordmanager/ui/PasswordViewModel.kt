package com.robert.passwordmanager.ui

import androidx.lifecycle.*
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.models.PasswordItem
import com.robert.passwordmanager.repositories.PasswordRepositoryImpl
import com.robert.passwordmanager.utils.PasswordManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val passwordManager: PasswordManager,
    private val repository: PasswordRepositoryImpl): ViewModel() {

    val allPasswords: LiveData<List<Account>> = repository.allPasswords.asLiveData()

    private var _orderBy = MutableLiveData<String>()

    val passwordItems = allPasswords.map { passwords->
        getPasswordItems(passwords)
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

    fun setOrderBY(new: String){
        _orderBy.value = new
    }

    fun insert(passwordDetails: Account) = viewModelScope.launch{
        repository.insert(passwordDetails)
    }

    fun delete(passwordDetails: Account) = viewModelScope.launch{
        repository.delete(passwordDetails)
    }

    fun evaluatePassword(password: String): Float {
        return passwordManager.evaluatePassword(password)
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
            sizeMap.put(category, size)
        }
        return sizeMap
    }

    private fun getPasswordItems(items: List<Account>): List<PasswordItem> {
        if (items.isEmpty()) return emptyList()

        val passwordTitles = items
            .groupBy { it.date }
            .map { (category, passwords) ->
                val passwordSum = passwords.sumOf { it.id }
                PasswordItem.PasswordTitle(category, passwordSum)
            }
        val passwordDetails = items.map { PasswordItem.Password(it) }

        return passwordTitles.flatMap { listOf(it) + passwordDetails.filter { p -> p.account.date == it.title } }
    }



}