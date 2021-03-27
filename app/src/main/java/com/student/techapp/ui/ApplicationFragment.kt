package com.student.techapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.student.techapp.R
import com.student.techapp.databinding.FragmentApplicationBinding


class ApplicationFragment : Fragment(R.layout.fragment_application) {

    private lateinit var binding: FragmentApplicationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentApplicationBinding.bind(view)


        binding.btnRequest.setOnClickListener {
            val action = ApplicationFragmentDirections.actionApplicationFragmentToRequestFragment()
            findNavController().navigate(action)
        }

    }


}