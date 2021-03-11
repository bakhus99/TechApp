package com.student.techapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.student.techapp.R
import com.student.techapp.databinding.FragmentMainBinding


class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        addNewMessage()
    }

    private fun addNewMessage() {
        binding.fabNewMsg.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToNewMessagesFragment()
            findNavController().navigate(action)
        }
    }
}