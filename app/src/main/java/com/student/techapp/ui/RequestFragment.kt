package com.student.techapp.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.student.techapp.R
import com.student.techapp.databinding.FragmentRequestBinding
import com.student.techapp.models.Request
import java.util.*


class RequestFragment : Fragment(R.layout.fragment_request) {

    private lateinit var binding: FragmentRequestBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRequestBinding.bind(view)

        binding.btnSend.setOnClickListener {
            sendToDb()
        }

    }

    private fun sendToDb() {
        val id = UUID.randomUUID().toString()
        val name = binding.etName.text.toString()
        val address = binding.etAddress.text.toString()
        val phone = binding.etPhone.text.toString()
        val service = binding.auTvChooseSetvice.text.toString()
        val problem = binding.etProblem.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/requests/$id")
        if (fromId == null) return
        val request = Request(fromId, name, address, phone, service, problem)

        ref.setValue(request).addOnSuccessListener {
            Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume() {
        super.onResume()
        val services = resources.getStringArray(R.array.services)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, services)
        binding.auTvChooseSetvice.setAdapter(arrayAdapter)
    }

}