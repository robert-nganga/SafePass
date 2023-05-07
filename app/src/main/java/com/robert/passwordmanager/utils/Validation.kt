package com.robert.passwordmanager.utils

object Validation {

    fun validatePassword(password: String): String?{
        return when {
            password.length < 8 -> {"Must be more than 8 characters"}
            !password.matches(".*[A-Z].*".toRegex()) -> {"Must contain Upper-case character"}
            !password.matches(".*[a-z].*".toRegex()) -> {"Must contain Lower-case character"}
            !password.matches(".*[0-9].*".toRegex()) -> {"Must contain a number"}
            !password.matches(".*[!@$#^%*()_><.,:;|}{~`+=].*".toRegex()) -> {"Must contain special character"}
            else -> {null}
        }
    }
}