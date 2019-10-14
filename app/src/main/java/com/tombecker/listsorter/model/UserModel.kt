package com.tombecker.listsorter.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "age")
    val age: Int
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}