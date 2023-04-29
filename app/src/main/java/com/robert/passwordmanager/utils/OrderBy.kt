package com.robert.passwordmanager.utils

sealed class OrderBy {
    object Date : OrderBy()
    object Category : OrderBy()
}