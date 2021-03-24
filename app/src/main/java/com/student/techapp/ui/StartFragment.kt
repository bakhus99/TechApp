package com.student.techapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.student.techapp.R
import com.student.techapp.databinding.FragmentStartBinding

class StartFragment : Fragment(R.layout.fragment_start) {

    private lateinit var binding: FragmentStartBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStartBinding.bind(view)

        val token = FirebaseMessaging.getInstance().token
        Log.d("Token", "onViewCreated: $token ")

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val action = StartFragmentDirections.actionStartFragmentToMainFragment()
            findNavController().navigate(action)
        }

        binding.btnLogin.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        binding.btnSkip.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToMainFragment()
            findNavController().navigate(action)
        }
        binding.tvRegister.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
        binding.btnSupport.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToAboutFragment()
            findNavController().navigate(action)
        }


    }
}