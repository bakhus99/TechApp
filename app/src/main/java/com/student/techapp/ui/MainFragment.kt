package com.student.techapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.student.techapp.R
import com.student.techapp.databinding.FragmentMainBinding
import com.student.techapp.models.Users
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        var currentUser: Users? = null
    }

    private lateinit var binding: FragmentMainBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        binding.rvChatLatestMesages.adapter = adapter
        addNewMessage()
        fetchCurrentUser()
    }

    private val adapter = GroupAdapter<GroupieViewHolder>()

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/profile/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(Users::class.java)
                Log.d("MainFragnent", "current User: ${currentUser?.username} ")
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun addNewMessage() {
        binding.fabNewMsg.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToNewMessagesFragment()
            findNavController().navigate(action)
        }
    }
}