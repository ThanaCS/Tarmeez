package com.thanaa.tarmeezapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.updateMarginsRelative
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.thanaa.tarmeezapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var aAuth:FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var newAccountTextView: TextView
    private lateinit var forgotPassword: TextView
    private lateinit var progressDialog:CustomProgressDialog
    private lateinit var loginButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?):View{
        hideNavigation()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        emailEditText = binding.userEmail
        passwordEditText = binding.userPassword
        newAccountTextView = binding.newAccount
        emailEditText.hint = getString(R.string.enter_email, "")
        passwordEditText.hint = getString(R.string.enter_password, " ")
        progressDialog = CustomProgressDialog(requireContext())
        loginButton = binding.login
        forgotPassword = binding.forgotPassword

        aAuth = FirebaseAuth.getInstance()

        newAccountTextView.setOnClickListener {
            Navigation.findNavController(_binding!!.root)
                .navigate(R.id.LoginFragmentToRegisterFragment)
        }

        forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

      loginButton.setOnFocusChangeListener { _, _ ->
          hideKeyBoard()
      }
      loginButton.setOnClickListener {
            if (validation()){
                progressDialog.show()
                aAuth = FirebaseAuth.getInstance()
                aAuth.signInWithEmailAndPassword(emailEditText.text.toString(),
                    passwordEditText.text.toString()).
                addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                        if (sharedPref != null) {
                            with (sharedPref.edit()) {
                                putString("email", emailEditText.text.toString())
                                apply()
                            }
                        }
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {
                        progressDialog.dismiss()
                        val snackBar = Snackbar.make(binding.root,
                            getString(R.string.error_message, " "), 3000)
                        val snackBarText: TextView = snackBar.view.
                        findViewById(com.google.android.material.R.id.snackbar_text)
                        snackBarText.textSize = 16f
                        val layoutParams = FrameLayout.LayoutParams(LinearLayout
                            .LayoutParams.WRAP_CONTENT, LinearLayout.
                        LayoutParams.WRAP_CONTENT, Gravity.RIGHT )
                        layoutParams.updateMarginsRelative(0, 1800,
                            0, 0)
                        snackBarText.setCompoundDrawablesWithIntrinsicBounds(
                           0, 0,
                            R.drawable.ic_baseline_error_outline_24, 0)
                        snackBar.view.setPadding(400, 0, 0, 0)
                        layoutParams.gravity = Gravity.CENTER
                        snackBar.view.layoutParams = layoutParams
                        snackBar.setBackgroundTint(resources.getColor(R.color.dark_gray))
                        snackBar.setActionTextColor(resources.getColor(R.color.white))
                        snackBar.show()
                    }
                }
            }
          hideKeyBoard()
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

    private fun hideKeyBoard() {
        activity?.let {
            val inputManager =
                it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = it.currentFocus
            if (view != null) {
                inputManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }
}