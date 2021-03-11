package com.student.techapp.adapter

import android.widget.TextView
import com.student.techapp.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatFromItem(val text:String):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.fromMessage).text = text
    }

    override fun getLayout() = R.layout.chat_from_row
}