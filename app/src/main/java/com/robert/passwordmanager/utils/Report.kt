package com.robert.passwordmanager.utils

data class Report(
    val total: Int,
    val strong: Int,
    val weak: Int,
    val reused: Int
)