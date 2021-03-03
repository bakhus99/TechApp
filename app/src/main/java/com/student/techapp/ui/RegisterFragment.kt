package com.student.techapp.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.student.techapp.DatePickerFragment
import com.student.techapp.R
import com.student.techapp.databinding.FragmentRegisterBinding
import org.w3c.dom.Text

private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val args: RegisterFragmentArgs by navArgs()
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        binding.datePicker.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val name = binding.etName.text.toString()
        val surname = binding.etSurname.text.toString()
        val middlename =binding.etMiddleName.text.toString()
//        val birthdayDate = args.date
        val city = binding.city.text.toString()
        val about = binding.aboutYourself.text.toString()


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
//            TextUtils.isEmpty(birthdayDate) ->{
//                binding.tvPickupDate.error = "Choose your birthday date"
//            }
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
                    Toast.makeText(context, "User reg success", Toast.LENGTH_SHORT).show()
                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment()
        activity?.let { newFragment.show(it.supportFragmentManager, "datePicker") }

    }

}