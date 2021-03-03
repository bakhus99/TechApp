package com.student.techapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.student.techapp.R
import com.student.techapp.databinding.FragmentStartBinding

class StartFragment : Fragment(R.layout.fragment_start) {

    private lateinit var binding: FragmentStartBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStartBinding.bind(view)

        binding.btnLogin.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        binding.btnSkip.setOnClickListener {

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