package com.student.techapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.student.techapp.R
import com.student.techapp.databinding.FragmentProfileBinding

class ProfileFragment:Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth:FirebaseAuth
    private var databaseReference :  DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        loadProfile()

    }

    private fun loadProfile() {
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)
        binding.tvCity.text = user?.email

        userReference?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.tvName.text = snapshot.child("name").value.toString()
                binding.tvSurname.text = snapshot.child("lastname").value.toString()
                binding.tvMiddleName.text = snapshot.child("middlename").value.toString()
                binding.tvCity.text = snapshot.child("city").value.toString()
                binding.tvAboutMe.text = snapshot.child("about").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Canceled",Toast.LENGTH_SHORT).show()
            }
        })
    }

}