package com.student.techapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.student.techapp.R
import com.student.techapp.adapter.UserItem
import com.student.techapp.data.Users
import com.student.techapp.databinding.FragmentNewMessagesBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class NewMessagesFragment : Fragment(R.layout.fragment_new_messages) {

    private lateinit var binding: FragmentNewMessagesBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewMessagesBinding.bind(view)
//        val adapter = GroupAdapter<GroupieViewHolder>()
//
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//
//        binding.rvNewMessages.adapter = adapter

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
                    if (user != null){
                        adapter.add(UserItem(user))
                    }
                }
                binding.rvNewMessages.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}