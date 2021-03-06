package com.student.techapp.ui

import android.app.DatePickerDialog
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.student.techapp.R
import com.student.techapp.databinding.FragmentRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


private const val TAG = "RegisterFragment"

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
                    ("" + dayOfMonth + " " + (monthOfYear + 1) + ", " + year)
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
                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun downloadDataFromFirebase(fileUri: Uri) {
        val ref = storageRef?.reference?.child("file.docx")
        val maxDownloadBytes: Long = 1024 * 1024
        try {
            val fileSnapshot = ref?.let { Tasks.await(it.getFile(fileUri)) }
            val bytesSnapshot = ref?.let { Tasks.await(it.getBytes(maxDownloadBytes)) }
            val streamSnapshot = ref?.let { Tasks.await(it.stream) }
            //Task success
        } catch (e: Exception) {
            //Manage error
        }
    }

    private fun downloadF() {
        storageRef?.reference?.child("file.docx")?.getBytes(Long.MAX_VALUE)
            ?.addOnSuccessListener { bytes -> // Use the bytes to display the image
                val path = Environment.getExternalStorageDirectory()
                //                    .toString() + "/" + editTextName.getText().toString()
                try {
                    val fos = FileOutputStream(path)
                    fos.write(bytes)
                    fos.close()
                    Toast.makeText(context, "Success!!!", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT)
                        .show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }?.addOnFailureListener {
                // Handle any errors
            }
    }

    private fun downloadLicense() = CoroutineScope(Dispatchers.IO).launch {

        try {
            val bytes = storageRef!!.reference.child("file.docx")
            val localFile = File.createTempFile("file", "docx")
            bytes.getFile(localFile).addOnSuccessListener {

                Toast.makeText(context, "Statred dwlnd", Toast.LENGTH_SHORT).show()

            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }


//        storageRef = FirebaseStorage.getInstance()
//        ref = storageRef!!.reference.child("file.docx")
//        ref!!.downloadUrl.addOnCompleteListener {
//            val url = it.toString()
//            downloadFile(url)
//        }.addOnFailureListener {
//
//        }
    }

    private fun downloadFile(
        url: String
    ) {
        val downloadManager =
            activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
            .setTitle("Title")
            .setDescription("Description")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        downloadManager.enqueue(request)

    }


}