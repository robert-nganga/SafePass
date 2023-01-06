package com.robert.passwordmanager

import java.security.SecureRandom
import java.util.logging.Logger

class PasswordManager {

    val letters : String = "abcdefghijklmnopqrstuvwxyz"
    val uppercaseLetters : String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val numbers : String = "0123456789"
    val special : String = "@#=+!£$%&?-"
    val maxPasswordLength : Float = 20F //Max password lenght that my app creates
    val maxPasswordFactor : Float = 10F //Max password factor based on chars inside password
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
    fun generatePassword(isWithLetters: Boolean,
                         isWithUppercase: Boolean,
                         isWithNumbers: Boolean,
                         isWithSpecial: Boolean,
                         length: Int) : String {

        var result = ""
        var i = 0

        if(isWithLetters){ result += this.letters }
        if(isWithUppercase){ result += this.uppercaseLetters }
        if(isWithNumbers){ result += this.numbers }
        if(isWithSpecial){ result += this.special }

        return if (result.isEmpty()){
            ""
        }else {
            val rnd = SecureRandom.getInstance("SHA1PRNG")
            val sb = StringBuilder(length)

            while (i < length) {
                val randomInt : Int = rnd.nextInt(result.length)
                sb.append(result[randomInt])
                i++
            }
            sb.toString()
        }
    }

    /**
     * Evaluate a random password
     * @param passwordToTest String with the password to test
     * @return a number from 0 to 1, 0 is a very bad password and 1 is a perfect password
     */
    fun evaluatePassword(passwordToTest: String) : Float {

        var factor = 0
        val length = passwordToTest.length

        if( passwordToTest.matches( Regex(".*["+this.letters+"].*") ) ) { factor += 2 }
        if( passwordToTest.matches( Regex(".*["+this.uppercaseLetters+"].*") ) ){ factor += 2 }
        if( passwordToTest.matches( Regex(".*["+this.numbers+"].*") ) ){ factor += 1 }
        if( passwordToTest.matches( Regex(".*["+this.special+"].*") ) ){ factor += 5 }

        return (factor*length)/(maxPasswordFactor*maxPasswordLength)
    }

}