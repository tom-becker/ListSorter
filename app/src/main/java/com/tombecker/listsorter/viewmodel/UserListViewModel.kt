package com.tombecker.listsorter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tombecker.listsorter.model.UserDatabase
import com.tombecker.listsorter.model.UserModel
import com.tombecker.listsorter.util.Constants.SortBy
import kotlinx.coroutines.*

class UserListViewModel(application: Application): AndroidViewModel(application) {

    val users = MutableLiveData<ArrayList<UserModel>>()

    private var sortBy = SortBy.ID

    fun getUsers(): ArrayList<UserModel>? {
        return  users.value
    }

    fun initUsers() {
        GlobalScope.launch {
            val localList = UserDatabase.getInstance(getApplication()).userDao().getAllUsers()
            if(localList.isNullOrEmpty()) {
                users.postValue(loadInitialUsers())
            } else {
                //TODO Find way to get Room to return an ArrayList
                val localArrayList = arrayListOf<UserModel>()
                localArrayList.addAll(localList)
                sortListBy(sortBy)
                users.postValue(localArrayList)
            }
        }
    }

    fun sortListBy(sort: SortBy) {
        sortBy = sort

        val newList = getUsers()
        when(sortBy) {
            SortBy.ID -> newList?.sortBy { it.uuid }
            SortBy.NAME -> newList?.sortBy { it.name }
            SortBy.EMAIL -> newList?.sortBy { it.email }
            SortBy.AGE -> newList?.sortBy { it.age }
        }
        users.postValue(newList)
    }

    private fun sortListAndPost(list: List<UserModel>) {
        //TODO there has to be a better way to do this
        val newList = arrayListOf<UserModel>()
        newList.addAll(list)

        when(sortBy) {
            SortBy.ID -> newList.sortBy { it.uuid }
            SortBy.NAME -> newList.sortBy { it.name }
            SortBy.EMAIL -> newList.sortBy { it.email }
            SortBy.AGE -> newList.sortBy { it.age }
        }
        users.postValue(newList)
    }

    private fun loadInitialUsers(): ArrayList<UserModel> {
        val nameList = arrayOf(
            "Tom",
            "Bob",
            "Susie",
            "Jeff",
            "Megan",
            "Zach",
            "Nancy",
            "Jack",
            "Matt",
            "Alex"
        )

        val emailList = arrayOf(
            "abc@gmail.com",
            "xyz@gmail.com",
            "lmn@gmail.com",
            "wxy@gmail.com",
            "def@gmail.com",
            "vwx@gmail.com",
            "fgh@gmail.com",
            "opq@gmail.com",
            "cde@gmail.com",
            "tuv@gmail.com"
        )

        val ageList = arrayOf(
            27,
            52,
            45,
            77,
            10,
            16,
            30,
            55,
            20,
            33
        )

        val newList = arrayListOf<UserModel>()
        for(i in nameList.indices) {
            addNewUser(
                nameList[i],
                emailList[i],
                ageList[i]
            )
        }

        return newList
    }

    fun addNewUser(name: String, email: String, age: Int) {
        GlobalScope.launch {
            UserDatabase.getInstance(getApplication())
                .userDao().insertUser(UserModel(
                    name,
                    email,
                    age
                )
            )
            sortListAndPost(UserDatabase.getInstance(getApplication())
                .userDao().getAllUsers())
        }
    }

    fun deleteUser(uuid: Int) {
        GlobalScope.launch {
            val newList = getUsers()
            newList?.remove(UserDatabase.getInstance(getApplication()).userDao().getUser(uuid))
            users.postValue(newList)
            UserDatabase.getInstance(getApplication()).userDao().deleteUser(uuid)
        }
    }
}