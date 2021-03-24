package com.student.techapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.student.techapp.R
import com.student.techapp.adapter.LatestMsgRow
import com.student.techapp.databinding.FragmentMainBinding
import com.student.techapp.models.ChatMessage
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
        binding.rvChatLatestMesages.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        adapter.setOnItemClickListener { item, view ->
            val row = item as LatestMsgRow

            val action = row.charPartnerUser?.let {
                MainFragmentDirections.actionMainFragmentToChatFragment(
                    it
                )
            }
            if (action != null) {
                findNavController().navigate(action)
            }

        }
        listenForNewMsg()
        addNewMessage()
        fetchCurrentUser()
    }

    private val adapter = GroupAdapter<GroupieViewHolder>()

    val latestMsgMap = HashMap<String, ChatMessage>()

    private fun listenForNewMsg() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return

                latestMsgMap[snapshot.key!!] = chatMessage
                refreshRvMsg()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return

                latestMsgMap[snapshot.key!!] = chatMessage
                refreshRvMsg()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun refreshRvMsg() {
        adapter.clear()
        latestMsgMap.values.forEach {
            adapter.add(LatestMsgRow(it))
        }
    }


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