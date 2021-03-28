package com.student.techapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.student.techapp.R
import com.student.techapp.adapter.ApplicationRequestItem
import com.student.techapp.databinding.FragmentApplicationBinding
import com.student.techapp.models.Request
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class ApplicationFragment : Fragment(R.layout.fragment_application) {

    private lateinit var binding: FragmentApplicationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentApplicationBinding.bind(view)



        fetchApplications()

        binding.btnRequest.setOnClickListener {
            val action = ApplicationFragmentDirections.actionApplicationFragmentToRequestFragment()
            findNavController().navigate(action)
        }

    }

    private fun fetchApplications() {
        val ref = FirebaseDatabase.getInstance().getReference("/requests")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach {
                    val applicationFromUser = it.getValue(Request::class.java)
                    if (applicationFromUser != null) {
                        adapter.add(ApplicationRequestItem(applicationFromUser))
                    }

                }
                binding.rvApplications.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}