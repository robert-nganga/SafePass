package com.robert.passwordmanager.ui

import android.util.Log
import androidx.lifecycle.*
import com.robert.passwordmanager.utils.PasswordManager
import com.robert.passwordmanager.repositories.PasswordRepositoryImpl
import com.robert.passwordmanager.models.PasswordDetails
import com.robert.passwordmanager.models.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val passwordManager: PasswordManager,
    private val repository: PasswordRepositoryImpl): ViewModel() {

    val allPasswords: LiveData<List<PasswordDetails>> = repository.allPasswords.asLiveData()

    fun searchPasswords(name: String): Flow<List<PasswordDetails>> = repository.searchPasswords(name)

    private val _generatedPassword = MutableLiveData<String>()
    val  generatedPassword: LiveData<String>
        get() = _generatedPassword


    fun generatePassword(isWithLetters: Boolean,
                         isWithNumbers: Boolean,
                         isWithSpecial: Boolean,
                         length: Int){
        val isUpperCase = isWithLetters
        _generatedPassword.value = passwordManager.generatePassword(isWithLetters,
             isUpperCase, isWithNumbers, isWithSpecial, length)
    }


    fun insert(passwordDetails: PasswordDetails) = viewModelScope.launch{
        repository.insert(passwordDetails)
    }

    fun delete(passwordDetails: PasswordDetails) = viewModelScope.launch{
        repository.delete(passwordDetails)
    }

    fun evaluatePassword(password: String): Float {
        return passwordManager.evaluatePassword(password)
    }

    fun getSizeOfEachCategory(categories: Array<String>, items: List<PasswordDetails> ): Map<String, Int>{
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

    fun getPasswordsByCategory(categories: Array<String>, items: List<PasswordDetails> ): ArrayList<Section> {
        val sections = ArrayList<Section>()
        categories.forEach { category->
            val passwordList = ArrayList<PasswordDetails>()
            items.forEach { item->
                if(item.category == category){
                    passwordList.add(item)
                }
            }
            if (passwordList.isNotEmpty()){
                sections.add(Section(category, passwordList))
                val size = passwordList.size
                Log.i("PasswordViewModel", "Section added with title $category and size $size")
            }
        }

        return sections
    }


}