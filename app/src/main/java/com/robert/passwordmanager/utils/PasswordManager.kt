package com.robert.passwordmanager.utils

import java.util.logging.Logger

class PasswordManager {

    private val letters : String = "abcdefghijklmnopqrstuvwxyz"
    private val uppercaseLetters : String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val numbers : String = "0123456789"
    private val special : String = "@#=+!£$%&?-"
    private val maxPasswordLength : Float = 20F //Max password lenght that my app creates
    private val maxPasswordFactor : Float = 10F //Max password factor based on chars inside password
    //  see evaluatePassword function below

    companion object {
        val logger = Logger.getLogger(PasswordManager::class.java.name)
    }


    fun generatePassword(
        isWithLetters: Boolean,
        isWithUppercase: Boolean,
        isWithNumbers: Boolean,
        isWithSpecial: Boolean,
        length: Int
    ): String {
        val charSets = mutableListOf<String>()

        if (isWithLetters) { charSets.add(this.letters) }
        if (isWithUppercase) { charSets.add(this.uppercaseLetters) }
        if (isWithNumbers) { charSets.add(this.numbers) }
        if (isWithSpecial) { charSets.add(this.special) }

        if (charSets.size < 1) { return "" }

        var result = charSets.joinToString("")
        if (isWithLetters && isWithUppercase && isWithNumbers && isWithSpecial) {
            // If all character sets are included, ensure at least one character from each set is included
            result += "${this.letters.random()}${this.uppercaseLetters.random()}${this.numbers.random()}${this.special.random()}"
        }

        result = result.toList().shuffled().joinToString("")

        return result.substring(0, length.coerceAtMost(result.length))
    }

    fun evaluatePassword(passwordToTest: String) : Float {

        var factor = 0
        val length = passwordToTest.length

        if( passwordToTest.matches( Regex(".*["+this.letters+"].*") ) ) { factor += 2 }
        if( passwordToTest.matches( Regex(".*["+this.uppercaseLetters+"].*") ) ){ factor += 3 }
        if( passwordToTest.matches( Regex(".*["+this.numbers+"].*") ) ){ factor += 1 }
        if( passwordToTest.matches( Regex(".*["+this.special+"].*") ) ){ factor += 4 }

        val result = (factor*length)/(maxPasswordFactor*maxPasswordLength)

        return result.coerceIn(0.0f .. 1.0f)
    }

}