package com.robert.passwordmanager.models

data class Section(
    val  sectionTitle: String,
    val itemList: ArrayList<PasswordDetails>)