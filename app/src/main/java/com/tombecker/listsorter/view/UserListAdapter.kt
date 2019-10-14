package com.tombecker.listsorter.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tombecker.listsorter.R
import com.tombecker.listsorter.model.UserModel
import kotlinx.android.synthetic.main.user_list_item.view.*

class UserListAdapter(private val userList: ArrayList<UserModel>,
                      private val userClick: (Int) -> Unit): RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.view.apply {
            id_tv.apply {
                text = userList[position].uuid.toString()
            }
            name_tv.apply {
                text = userList[position].name
            }
            email_tv.apply {
                text = userList[position].email
            }
            age_tv.apply {
                text = userList[position].age.toString()
            }
            setOnClickListener {
                userClick(userList[position].uuid)
            }
        }
    }

    fun refreshList(newList: List<UserModel>) {
        userList.clear()
        userList.addAll(newList)
        notifyDataSetChanged()
    }

    class UserViewHolder(var view: View): RecyclerView.ViewHolder(view)
}