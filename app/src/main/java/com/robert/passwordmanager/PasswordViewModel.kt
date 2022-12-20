package com.robert.passwordmanager

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.robert.passwordmanager.models.PasswordDetails
import com.robert.passwordmanager.models.Section
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PasswordViewModel(private val repository: PasswordRepository): ViewModel() {

    val allPasswords: LiveData<List<PasswordDetails>> = repository.allPasswords.asLiveData()

    fun searchPasswords(name: String): Flow<List<PasswordDetails>> = repository.searchPasswords(name)

    private val _generatedPassword = MutableLiveData<String>()
    val  generatedPassword: LiveData<String>
        get() = _generatedPassword


    fun generatePassword(isWithLetters: Boolean,
                         isWithNumbers: Boolean,
                         isWithSpecial: Boolean,
                         length: Int){
        val passwordManager = PasswordManager()
        _generatedPassword.value = passwordManager.generatePassword(isWithLetters,
             true, isWithNumbers, isWithSpecial, length)
    }


    fun insert(passwordDetails: PasswordDetails) = viewModelScope.launch{
        repository.insert(passwordDetails)
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




    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return PasswordViewModel(
                    (application as PasswordsApplication).repository,
                ) as T
            }
        }
    }
}