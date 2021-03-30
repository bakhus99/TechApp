package com.student.techapp.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.student.techapp.R
import com.student.techapp.databinding.FragmentProfileBinding

class ProfileFragment:Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private var filePath: Uri? = null
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private val REQUEST_IMAGE_CODE = 123

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        loadProfile()

        binding.btnUpload.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val action = ProfileFragmentDirections.actionProfileFragmentToStartFragment()
            findNavController().navigate(action)
        }
    }


    private fun loadProfile() {
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)

        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.tvName.text = snapshot.child("username").value.toString()
                binding.tvSurname.text = snapshot.child("usersurname").value.toString()
                binding.tvMiddleName.text = snapshot.child("address").value.toString()
                binding.tvCity.text = snapshot.child("city").value.toString()
                binding.tvAboutMe.text = snapshot.child("about").value.toString()
                binding.tvBirthday.text = snapshot.child("birthday").value.toString()
                val uri = snapshot.child("profileImage").value.toString()
                Glide.with(requireContext())
                    .load(uri)
                    .transform(
                        RoundedCorners(
                            requireContext().resources.getDimension(R.dimen.small).toInt()
                        )
                    )
                    .into(binding.circleImage)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()
            }
        })
    }

}