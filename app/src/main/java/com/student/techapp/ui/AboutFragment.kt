package com.student.techapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.student.techapp.R
import com.student.techapp.databinding.FragmentAboutBinding

class AboutFragment : Fragment(R.layout.fragment_about) {

    private lateinit var binding: FragmentAboutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAboutBinding.bind(view)

        binding.btnPhone.setOnClickListener {
            dialPhone()
        }
        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            sendEmail(message, "support@app.kz")
        }
        binding.btnEmail.setOnClickListener {
            sendEmail(null,"support@app.kz")
        }
    }

    private fun dialPhone() {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + "+77013299400")
        startActivity(dialIntent)
    }

    private fun sendEmail(message: String?, email: String) {
        val mailToIntent = Intent(Intent.ACTION_SEND)
        mailToIntent.data = Uri.parse("mailto:")
        mailToIntent.type = "text/plain"
        mailToIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        mailToIntent.putExtra(Intent.EXTRA_TEXT, message)
        try {
            startActivity(Intent.createChooser(mailToIntent, "Choose email client"))
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}