package com.student.techapp.ui

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.student.techapp.R
import com.student.techapp.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class ProfileFragment:Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private var filePath: Uri? = null
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private val REQUEST_IMAGE_CODE = 123

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        loadProfile()

        binding.userImage.setOnClickListener {
            updateUserImage()
        }
        binding.btnUpload.setOnClickListener {
            uploadFile()
        }
    }


    private fun loadProfile() {
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)
        binding.tvCity.text = user?.email

        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.tvName.text = snapshot.child("name").value.toString()
                binding.tvSurname.text = snapshot.child("lastname").value.toString()
                binding.tvMiddleName.text = snapshot.child("middlename").value.toString()
                binding.tvCity.text = snapshot.child("city").value.toString()
                binding.tvAboutMe.text = snapshot.child("about").value.toString()
                binding.tvBirthday.text = snapshot.child("bday").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUserImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Choose Image"), REQUEST_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {

            filePath = data.data!!
            try {
                filePath.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver,
                            filePath
                        )
                        binding.userImage.setImageBitmap(bitmap)
                    } else {
                        val source = activity?.let { it1 ->
                            ImageDecoder.createSource(
                                it1.contentResolver,
                                filePath!!
                            )
                        }
                        val bitmap = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                        binding.userImage.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadFile() = CoroutineScope(Dispatchers.IO).launch{
try {
    val randomKey = UUID.randomUUID().toString()

    val imageRef = FirebaseStorage.getInstance().reference.child("images/$randomKey")
        .putFile(filePath!!).await()
    withContext(Dispatchers.Main) {
        Toast.makeText(context, "File uploaded", Toast.LENGTH_SHORT).show()

    }
} catch (e: Exception) {
    withContext(Dispatchers.Main) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
}
    }
}