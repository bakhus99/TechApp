package com.student.techapp.ui

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.student.techapp.R
import com.student.techapp.databinding.FragmentRegisterBinding
import java.util.*

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var storageRef: FirebaseStorage? = null
    private var ref: StorageReference? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        val spannableString = SpannableString(getString(R.string.i_read_terms_of_use))
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        binding.tvLicense.text = spannableString
        binding.tvLicense.setTextColor(Color.BLUE)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storageRef = FirebaseStorage.getInstance()
        databaseReference = database?.reference!!.child("profile")


        binding.tvLicense.setOnClickListener {

            val action = RegisterFragmentDirections.actionRegisterFragmentToPdfViewFragment()
            findNavController().navigate(action)
        }
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.datePicker.setOnClickListener {
            val dpd = DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
                binding.tvSelectedDate.text =
                    ("" + dayOfMonth + "." + (monthOfYear + 1) + "." + year)
            }, year, month, day)
            dpd.show()

            binding.btnRegister.setOnClickListener {
                register()
            }
        }
    }


    private fun register() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val name = binding.etName.text.toString()
        val surname = binding.etSurname.text.toString()
        val middlename = binding.etMiddleName.text.toString()
        val birthdayDate = binding.tvSelectedDate.text.toString()
        val city = binding.city.text.toString()
        val about = binding.aboutYourself.text.toString()

        if (
            binding.checkbox.isChecked
        ) {
            when {
                TextUtils.isEmpty(binding.etEmail.text.toString()) -> {
                    binding.etEmail.error = "Enter email please"

                }
                TextUtils.isEmpty(binding.etPassword.text.toString()) -> {
                    binding.etPassword.error = "Please enter password"
                }
                TextUtils.isEmpty(name) -> {
                    binding.etName.error = "Please enter Name"
                }
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val currentUser = auth.currentUser
                        val currentUSerDb = databaseReference?.child((currentUser?.uid!!))
                        currentUSerDb?.child("name")?.setValue(name)
                        currentUSerDb?.child("lastname")?.setValue(surname)
                        currentUSerDb?.child("middlename")?.setValue(middlename)
                        currentUSerDb?.child("city")?.setValue(city)
                        currentUSerDb?.child("about")?.setValue(about)
                        currentUSerDb?.child("bday")?.setValue(birthdayDate)
                        Toast.makeText(context, "User reg success", Toast.LENGTH_SHORT).show()
                        val action =
                            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                        findNavController().navigate(action)
                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Please read term of use", Toast.LENGTH_SHORT).show()
        }
    }
}