package com.robert.passwordmanager.models

sealed class PasswordItem {
    data class Password(val account: Account): PasswordItem() {
        override val id: Int
            get() = account.id
    }
    data class PasswordTitle(val title: String, val titleId: Int): PasswordItem(){
        override val id: Int
            get() = titleId
    }
    abstract val id: Int
}