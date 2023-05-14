package com.robert.passwordmanager.utils

import java.util.logging.Logger

class PasswordManager {

    private val letters: String = "abcdefghijklmnopqrstuvwxyz"
    private val uppercaseLetters: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val numbers: String = "0123456789"
    private val special: String = "@#=+!£$%&?-"
    private val maxPasswordLength: Float = 20F //Max password lenght that my app creates
    private val maxPasswordFactor: Float = 10F //Max password factor based on chars inside password
    //  see evaluatePassword function below

    companion object {
        val logger = Logger.getLogger(PasswordManager::class.java.name)
    }

    /**
     * Generate a random password
     * @param isWithLetters Boolean value to specify if the password must contain letters
     * @param isWithUppercase Boolean value to specify if the password must contain uppercase letters
     * @param isWithNumbers Boolean value to specify if the password must contain numbers
     * @param isWithSpecial Boolean value to specify if the password must contain special chars
     * @param length Int value with the length of the password
     * @return the new password.
     */
    fun generatePassword(
        isWithLetters: Boolean,
        isWithUppercase: Boolean,
        isWithNumbers: Boolean,
        isWithSpecial: Boolean,
        length: Int
    ): String {
        val charSets = mutableListOf<String>()

        if (isWithLetters) {
            charSets.add(this.letters)
        }
        if (isWithUppercase) {
            charSets.add(this.uppercaseLetters)
        }
        if (isWithNumbers) {
            charSets.add(this.numbers)
        }
        if (isWithSpecial) {
            charSets.add(this.special)
        }

        if (charSets.size < 1) {
            return ""
        }

        var result = charSets.joinToString("")
        if (isWithLetters && isWithUppercase && isWithNumbers && isWithSpecial) {
            // If all character sets are included, ensure at least one character from each set is included
            result += "${this.letters.random()}${this.uppercaseLetters.random()}${this.numbers.random()}${this.special.random()}"
        }

        result = result.toList().shuffled().joinToString("")

        return result.substring(0, length.coerceAtMost(result.length))
    }


    /**
     * Evaluate a random password
     * @param passwordToTest String with the password to test
     * @return a number from 0 to 1, 0 is a very bad password and 1 is a perfect password
     */
    fun evaluatePassword(passwordToTest: String): Float {
        val length = passwordToTest.length
        var factor = 0f

        // Check for each type of character in the password
        for (character in passwordToTest) {
            when (character) {
                in letters -> {
                    factor += 2f
                }
                in uppercaseLetters -> {
                    factor += 2f
                }
                in numbers -> {
                    factor += 1f
                }
                in special -> {
                    factor += 5f
                }
            }
        }

        val score =  factor / (length * 5f)

        // Return the score
        return score.coerceIn(0.0f .. 1.0f)
    }
}
