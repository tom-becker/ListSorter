package com.tombecker.listsorter.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {

    @Insert
    fun insertUser(vararg question: UserModel): List<Long>

    @Query("SELECT * FROM usermodel WHERE uuid == :id")
    fun getUser(id: Int): UserModel

    @Query("SELECT * FROM usermodel")
    fun getAllUsers(): List<UserModel>

    @Query("DELETE FROM usermodel WHERE uuid == :id")
    fun deleteUser(id: Int): Int

}