package com.student.techapp.adapter

import android.widget.TextView
import com.student.techapp.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatToItem(val text:String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.toMessage).text = text
    }

    override fun getLayout() = R.layout.chat_to_row
}