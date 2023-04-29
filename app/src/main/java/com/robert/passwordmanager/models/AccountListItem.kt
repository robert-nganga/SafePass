package com.robert.passwordmanager.models

sealed class AccountListItem {
    data class AccountItem(val account: Account): AccountListItem() {
        override val id: Int
            get() = account.id
    }
    data class AccountHeaderItem(val title: String, val titleId: Int): AccountListItem(){
        override val id: Int
            get() = titleId
    }
    abstract val id: Int
}