package com.thanaa.tarmeezapp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.thanaa.tarmeezapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var aAuth:FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var newAccountTextView: TextView
    private lateinit var progressDialog:CustomProgressDialog
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?):View{
        hideNavigation()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        emailEditText = binding.userEmail
        passwordEditText = binding.userPassword
        newAccountTextView = binding.newAccount
        progressDialog = CustomProgressDialog(requireContext())
        alertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setMessage(getString(R.string.error_message))

        newAccountTextView.setOnClickListener {
            Navigation.findNavController(_binding!!.root)
                .navigate(R.id.LoginFragmentToRegisterFragment)
        }

        binding.login.setOnClickListener {
            if (validation()){
                progressDialog.show()
                aAuth = FirebaseAuth.getInstance()
                aAuth.signInWithEmailAndPassword(emailEditText.text.toString(),
                    passwordEditText.text.toString()).
                addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {

                        progressDialog.dismiss()
                        alertDialog.show()
                    }
                }
            }
        }
        return binding.root
    }
    //hide navigation button
    private fun hideNavigation() {
        val bottomNavigationView = (activity as MainActivity).bottomNavigationView
        val fab = (activity as MainActivity).fab
        val bottomAppBar = (activity as MainActivity).bottomAppBar
        bottomNavigationView.visibility = View.GONE
        bottomAppBar.visibility = View.GONE
        fab.visibility = View.GONE
    }
    private fun validation():Boolean{
        var result = true
        if(emailEditText.text.isBlank()){
            emailEditText.error = getString(R.string.enter_your_email)
            result = false
        }
        if(passwordEditText.text.toString().isBlank()){
            passwordEditText.error = getString(R.string.enter_your_password)
            result = false
        }
        if(!isEmailValid(emailEditText.text.toString())){
            emailEditText.error = getString(R.string.invalid_email)
        }
        if(emailEditText.text.isBlank() && passwordEditText.text.isBlank() ){
            emailEditText.error = getString(R.string.enter_your_email)
            passwordEditText.error = getString(R.string.enter_your_password)
            result = false
        }
        return result
    }
    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}