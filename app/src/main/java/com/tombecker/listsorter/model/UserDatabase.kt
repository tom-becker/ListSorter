package com.tombecker.listsorter.model

import android.content.Context
import androidx.room.*

@Database(entities = [UserModel::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO

    companion object {
        @Volatile
        private var instance: UserDatabase? = null

        private val LOCK = Any()

        fun getInstance(context: Context) = instance?: synchronized(LOCK) {
            instance ?: buildDb(context).also {
                instance = it
            }
        }

        private fun buildDb(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDatabase::class.java,
            "userDatabase"
        ).build()
    }
}