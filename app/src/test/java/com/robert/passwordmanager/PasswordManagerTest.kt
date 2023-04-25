package com.robert.passwordmanager

import com.robert.passwordmanager.utils.PasswordManager
import org.junit.Before
import org.junit.Test

class PasswordManagerTest{

    private lateinit var passwordManager: PasswordManager

    @Before
    fun setup() {
        passwordManager = PasswordManager()
    }

    @Test
    fun allCharacterTypesSelected_returnsStringOfLengthTen() {
        val length = 10
        val result = passwordManager.generatePassword(true, true, true, true, length)
        assert(result.length == length)
    }

    @Test
    fun noCharacterTypeSelected_returnsEmptyString() {
        val length = 10
        val result = passwordManager.generatePassword(false, false, false, false, length)
        assert(result.isEmpty())
    }

    @Test
    fun onlyOneCharacterTypeSelected_returnsStringOfLengthTen() {
        val length = 10
        val result = passwordManager.generatePassword(false, false, false, true, length)
        assert(result.length == length)
    }

    @Test
    fun onlyNumbersSelected_returnsOnlyNumbers() {
        val result = passwordManager.generatePassword(false, false, true, false, 10)
        assert(result.matches(Regex("[0-9]+")))
    }
}