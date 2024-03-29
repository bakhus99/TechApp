package com.student.techapp.ui.registerlogin

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.student.techapp.R
import com.student.techapp.databinding.FragmentRegisterBinding
import com.student.techapp.models.Users
import java.util.*

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var storageRef: FirebaseStorage? = null
    var selectedPhotoUri: Uri? = null



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

        binding.selectPhoto.setOnClickListener {
            selectPhoto()
        }

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

        if (
            binding.checkbox.isChecked
        ) {
            when {
                TextUtils.isEmpty(binding.etEmail.text.toString()) -> {
                    binding.etLayoutEmail.error = "Enter email please"

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
                        Toast.makeText(context, "User reg success", Toast.LENGTH_SHORT).show()
                        uploadImageToFirebase()
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

    private fun uploadImageToFirebase() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!).addOnSuccessListener { path ->
            Toast.makeText(
                context,
                "Sucsses photo update ${path.metadata?.path}",
                Toast.LENGTH_SHORT
            )
                .show()
            ref.downloadUrl.addOnSuccessListener {
                //acsess to file locatioan
                saveUserToFirebase(it.toString())
            }.addOnFailureListener {

            }
        }
    }

    private fun saveUserToFirebase(profileImgUrl: String) {
        val name = binding.etName.text.toString()
        val surname = binding.etSurname.text.toString()
        val address = binding.etRegAddress.text.toString()
        val birthdayDate = binding.tvSelectedDate.text.toString()
        val city = binding.city.text.toString()
        val about = binding.aboutYourself.text.toString()
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/profile/$uid")
        val user = Users(uid, profileImgUrl, name, surname, address, birthdayDate, city, about)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("REGISTERACTIVITY", "finally we saved user to firebase")
            }
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data

            val bitmap =
                MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedPhotoUri)
            binding.circleImage.setImageBitmap(bitmap)
            binding.selectPhoto.alpha = 0f
        }

    }
}