package com.student.techapp.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.student.techapp.R
import com.student.techapp.databinding.FragmentRequestBinding


class RequestFragment : Fragment(R.layout.fragment_request) {

    private lateinit var binding: FragmentRequestBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRequestBinding.bind(view)



    }

    override fun onResume() {
        super.onResume()
        val services = resources.getStringArray(R.array.services)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, services)
        binding.auTvChooseSetvice.setAdapter(arrayAdapter)
    }

}