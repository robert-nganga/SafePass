package com.robert.passwordmanager.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class PasswordManagerTest{

    private lateinit var passwordManager: PasswordManager

    @Before
    fun setup() {
        passwordManager = PasswordManager()
    }

    @Test
    fun generatePassword_allCharacterTypesSelected_returnsStringOfLengthTen() {
        val length = 10
        val result = passwordManager.generatePassword(true,
            isWithUppercase = true,
            isWithNumbers = true,
            isWithSpecial = true,
            length = length
        )
        assert(result.length == length)
    }

    @Test
    fun generatePassword_noCharacterTypeSelected_returnsEmptyString() {
        val length = 10
        val result = passwordManager.generatePassword(
            isWithLetters = false,
            isWithUppercase = false,
            isWithNumbers = false,
            isWithSpecial = false,
            length = length
        )
        assert(result.isEmpty())
    }

    @Test
    fun generatePassword_onlyOneCharacterTypeSelected_returnsStringOfLengthTen() {
        val length = 10
        val result = passwordManager.generatePassword(
            isWithLetters = false,
            isWithUppercase = false,
            isWithNumbers = false,
            isWithSpecial = true,
            length = length
        )
        assert(result.length == length)
    }

    @Test
    fun generatePassword_onlyNumbersSelected_returnsOnlyNumbers() {
        val result = passwordManager.generatePassword(
            isWithLetters = false,
            isWithUppercase = false,
            isWithNumbers = true,
            isWithSpecial = false,
            length = 10
        )
        assert(result.matches(Regex("[0-9]+")))
    }

    @Test
    fun generatePassword_onlyLettersSelected_returnsOnlyLetters() {
        val result = passwordManager.generatePassword(
            isWithLetters = true,
            isWithUppercase = false,
            isWithNumbers = false,
            isWithSpecial = false,
            length = 10
        )
        assert(result.matches(Regex("[a-z]+")))
    }

    @Test
    fun evaluatePassword_emptyString_returnsZero(){
        val result = passwordManager.evaluatePassword("")
        assertThat(result).isEqualTo(0.0f)
    }

    @Test
    fun evaluatePassword_onlyLowerCaseLetters_returnsCorrectScore(){
        val result = passwordManager.evaluatePassword("gsjagdjutwhbs")
        assertThat(result).isLessThan(0.2f)
    }

    @Test
    fun evaluatePassword_onlyNumbers_returnsCorrectScore(){
        val result = passwordManager.evaluatePassword("363764892734565")
        assertThat(result).isLessThan(0.1f)
    }

    @Test
    fun evaluatePassword_onlyUpperCase_returnsCorrectScore(){
        val result = passwordManager.evaluatePassword("ASDWRETRWDEQGAB")
        assertThat(result).isGreaterThan(0.2f)
    }

    @Test
    fun evaluatePassword_allCharacters_returnsCorrectScore(){
        val result = passwordManager.evaluatePassword("AdAd@#%5aDsE$65!d#")
        assertThat(result).isGreaterThan(0.8f)
    }

}