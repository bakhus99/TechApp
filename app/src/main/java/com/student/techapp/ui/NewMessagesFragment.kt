package com.student.techapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.student.techapp.R
import com.student.techapp.adapter.UserItem
import com.student.techapp.databinding.FragmentNewMessagesBinding
import com.student.techapp.models.Users
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class NewMessagesFragment : Fragment(R.layout.fragment_new_messages) {

    private lateinit var binding: FragmentNewMessagesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewMessagesBinding.bind(view)

        fetchUsers()

    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/profile")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach {
                    Log.d("NewMsg", "${it.toString()} ")
                    val user = it.getValue(Users::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val user = userItem.users
                    val action =
                        NewMessagesFragmentDirections.actionNewMessagesFragmentToChatFragment(
                           user
                        )
                    findNavController().navigate(action)
                }
                binding.rvNewMessages.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


}