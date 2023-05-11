package com.robert.passwordmanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "password_table")
data class Account(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name ="password")val password: String,
    @ColumnInfo(name ="category")val category: String,
    @ColumnInfo(name ="date")val date: String,
    @ColumnInfo(name ="user_name") val userName: String,
    @ColumnInfo(name ="website_name") val websiteName: String
){

}