package com.robert.passwordmanager

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PasswordManagerTest{

    private lateinit var passwordManager: PasswordManager

    @Before
    fun setup() {
        passwordManager = PasswordManager()
    }

    @Test
    fun generatesCharacterOfSpecifiedLength_returnsTrue() {
        val length = 10
        val result = passwordManager.generatePassword(true, true, true, true, length)
        assert(result.length == length)
    }
}