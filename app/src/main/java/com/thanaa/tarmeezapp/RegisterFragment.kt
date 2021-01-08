package com.thanaa.tarmeezapp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.thanaa.tarmeezapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var aAuth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordConfirmEditText: EditText
    private lateinit var loginTextView: TextView
    private lateinit var registerButton: Button
    private lateinit var progressDialog:CustomProgressDialog
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View{
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        emailEditText = binding.userEmail
        passwordEditText = binding.userPassword
        passwordConfirmEditText = binding.passwordConfirmation
        registerButton = binding.register
        loginTextView = binding.hasAccount
        progressDialog = CustomProgressDialog(requireContext())
        alertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setMessage(getString(R.string.already_exist))

        registerButton.setOnClickListener {
            if(validation()){
                progressDialog.show()
                aAuth = FirebaseAuth.getInstance()
                aAuth.createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()).
                addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.RegisterFragmentToHomeFragment)
                    } else {
                        progressDialog.dismiss()
                        alertDialog.show()
                    }
                }
            }
        }

        loginTextView.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.RegisterFragmentToLoginFragment)
        }
        return binding.root
    }

    //input validation function
    private fun validation():Boolean{
        var result = true
        if(emailEditText.text.isBlank()){
            emailEditText.error = getString(R.string.enter_your_email)
            result = false
        }
        if(!isEmailValid(emailEditText.text.toString())){
            emailEditText.error = getString(R.string.invalid_email)
        }
        if(passwordEditText.text.isBlank()){
            passwordEditText.error = getString(R.string.enter_your_password)
            result = false
        }
        if(emailEditText.text.isBlank() && passwordEditText.text.isBlank() ){
            emailEditText.error = getString(R.string.enter_your_email)
            passwordEditText.error = getString(R.string.enter_your_password)
            result = false
        }
        if(passwordEditText.text.toString() != passwordConfirmEditText.text.toString()){
            passwordConfirmEditText.error = getString(R.string.un_matched_password)
            result = false
        }else if( passwordEditText.text.toString().length < 6 ){
            passwordConfirmEditText.error = getString(R.string.password_more_than_six)
            result = false
        }
        return result
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}