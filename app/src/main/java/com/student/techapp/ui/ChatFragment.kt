package com.student.techapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.student.techapp.R
import com.student.techapp.adapter.ChatFromItem
import com.student.techapp.adapter.ChatToItem
import com.student.techapp.databinding.FragmentChatBinding
import com.student.techapp.models.ChatMessage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var binding: FragmentChatBinding
    private val args: ChatFragmentArgs by navArgs()

    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)

        binding.rvChatMsg.adapter = adapter

        // setupDummeData()
        listenForMessages()

        binding.btnSendMsg.setOnClickListener {
            performSendMsg()
        }

    }

    private fun listenForMessages() {
        val reference = FirebaseDatabase.getInstance().getReference("/messages")
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.d("New msg", chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatFromItem(chatMessage.text))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text))
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun performSendMsg() {
        val msg = binding.etEnterMessage.text.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = args.uid
        if (fromId == null) return

        val chatMessage =
            ChatMessage(ref.key!!, msg, fromId, toId, System.currentTimeMillis() / 1000)

        ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("Perform msg", "Saved our msg: ${ref.key}")
            }
    }

    private fun setupDummeData() {
        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatFromItem("From love"))
        adapter.add(ChatToItem("With love \n to you"))
        adapter.add(ChatFromItem("From love"))
        adapter.add(ChatToItem("With love \n to you"))
        adapter.add(ChatFromItem("From love"))
        adapter.add(ChatToItem("With love \n to you"))

        binding.rvChatMsg.adapter = adapter
    }


}