package com.student.techapp.adapter

import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.student.techapp.R
import com.student.techapp.models.ChatMessage
import com.student.techapp.models.Users
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class LatestMsgRow(val chatMsg: ChatMessage) : Item<GroupieViewHolder>() {

    var charPartnerUser:Users? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val tvLatestMsg = viewHolder.itemView.findViewById<TextView>(R.id.tvLatestMsg)
        val tvUserName = viewHolder.itemView.findViewById<TextView>(R.id.tvUserNameLatestMsg)
        val userImage = viewHolder.itemView.findViewById<CircleImageView>(R.id.profileImageLatestMsg)
        val chatPartnerId: String = if (chatMsg.fromId == FirebaseAuth.getInstance().uid) {
            chatMsg.toId
        } else {
            chatMsg.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/profile/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val charPartnerUser = snapshot.getValue(Users::class.java)
                tvUserName.text = charPartnerUser?.username

                Glide.with(viewHolder.itemView.context).load(charPartnerUser?.profileImage).into(userImage)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        tvLatestMsg.text = chatMsg.text


    }

    override fun getLayout() = R.layout.latest_msg_row
}