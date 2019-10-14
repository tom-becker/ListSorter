package com.tombecker.listsorter.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tombecker.listsorter.R
import com.tombecker.listsorter.model.UserModel
import com.tombecker.listsorter.util.Constants
import com.tombecker.listsorter.viewmodel.UserListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: UserListViewModel

    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this)[UserListViewModel::class.java].apply {
            initUsers()
        }

        initView()
        subscribeToViewModel()
    }

    private fun initView() {
        userListAdapter = UserListAdapter(viewModel.getUsers() ?: arrayListOf()) {
            showDeleteUserDialog(it)
        }

        user_list.apply {
            adapter = userListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        radio_group_list_sort.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.radio_name -> viewModel.sortListBy(Constants.SortBy.NAME)
                R.id.radio_email -> viewModel.sortListBy(Constants.SortBy.EMAIL)
                R.id.radio_age -> viewModel.sortListBy(Constants.SortBy.AGE)
            }
        }

        add_user_fab.setOnClickListener {
            showNewUserDialog()
        }
    }

    private fun showNewUserDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.new_user_dialog_view, null)
        builder.setTitle(R.string.new_user_dialog_title)
        builder.setView(dialogView)

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            val newUserName = dialogView.findViewById<EditText>(R.id.new_user_name).text.toString()
            val newUserEmail = dialogView.findViewById<EditText>(R.id.new_user_email).text.toString()
            val newUserAge = dialogView.findViewById<EditText>(R.id.new_user_age).text.toString().toInt()
            viewModel.addNewUser(newUserName, newUserEmail, newUserAge)
            dialog.dismiss()
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showDeleteUserDialog(uuid: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.delete_user_dialog_title)

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            viewModel.deleteUser(uuid)
            dialog.dismiss()
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun subscribeToViewModel() {
        viewModel.users.observe(this, Observer<List<UserModel>> { users ->
            userListAdapter.refreshList(users)
        })
    }
}
