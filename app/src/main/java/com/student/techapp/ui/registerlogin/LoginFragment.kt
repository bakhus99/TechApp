package com.student.techapp.ui.registerlogin

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.student.techapp.R
import com.student.techapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
        val currentUser = auth.currentUser
        if (currentUser != null){
            val action = LoginFragmentDirections.actionLoginFragmentToProfileFragment()
            findNavController().navigate(action)
        }
    }

    private fun loginUser() {
        if (TextUtils.isEmpty(binding.login.text.toString())){
            Toast.makeText(context,"please chenk your email",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(binding.password.text.toString())){
            Toast.makeText(context,"please chenk your password",Toast.LENGTH_SHORT).show()
        }
        auth.signInWithEmailAndPassword(binding.login.text.toString(),binding.password.text.toString())
            .addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(context,"Logged in",Toast.LENGTH_SHORT).show()
                    val action = LoginFragmentDirections.actionLoginFragmentToProfileFragment()
                    findNavController().navigate(action)
                }else{
                    Toast.makeText(context," Try again ",Toast.LENGTH_SHORT).show()
                }
            }
    }

}