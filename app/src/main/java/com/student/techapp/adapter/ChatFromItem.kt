package com.student.techapp.adapter

import android.widget.TextView
import com.bumptech.glide.Glide
import com.student.techapp.R
import com.student.techapp.models.Users
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class ChatFromItem(val text:String,val user:Users):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.fromMessage).text = text
        val uri = user.profileImage
        val image = viewHolder.itemView.findViewById<CircleImageView>(R.id.userImageFrom )
        Glide.with(viewHolder.itemView.context).load(uri).into(image)
    }

    override fun getLayout() = R.layout.chat_from_row
}