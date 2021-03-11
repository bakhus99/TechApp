package com.student.techapp.adapter

import android.widget.TextView
import com.bumptech.glide.Glide
import com.student.techapp.R
import com.student.techapp.models.Users
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class UserItem(val users: Users):Item<GroupieViewHolder>() {


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.userName).text = users.username
        Glide.with(viewHolder.itemView.context).load(users.profileImage).into(viewHolder.itemView.findViewById(R.id.profileUserImage))
    }

    override fun getLayout() = R.layout.user_row_new_msg

}