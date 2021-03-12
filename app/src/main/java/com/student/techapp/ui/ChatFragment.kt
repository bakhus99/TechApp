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
import com.student.techapp.models.Users
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var binding: FragmentChatBinding
    private val args: ChatFragmentArgs by navArgs()

    val adapter = GroupAdapter<GroupieViewHolder>()
    var toUser: Users? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)

        binding.rvChatMsg.adapter = adapter
        toUser = args.user
        listenForMessages()

        binding.btnSendMsg.setOnClickListener {
            performSendMsg()
        }

    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.d("New msg", chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = MainFragment.currentUser ?: return
                        adapter.add(ChatFromItem(chatMessage.text, currentUser))
                    } else {
                        //val toUser = args.user
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


    private fun performSendMsg() {
        val msg = binding.etEnterMessage.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        // mb tut error budet
        val toId = toUser!!.uid
        if (fromId == null) return
        //val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val torRef = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val chatMessage =
            ChatMessage(ref.key!!, msg, fromId, toId, System.currentTimeMillis() / 1000)

        ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("Perform msg", "Saved our msg: ${ref.key}")
                binding.etEnterMessage.text?.clear()
                binding.rvChatMsg.scrollToPosition(adapter.itemCount -1)
            }
        torRef.setValue(chatMessage)
    }


}