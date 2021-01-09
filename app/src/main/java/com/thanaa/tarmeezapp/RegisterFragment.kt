package com.thanaa.tarmeezapp


import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.updateMarginsRelative
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thanaa.tarmeezapp.data.User
import com.thanaa.tarmeezapp.databinding.FragmentRegisterBinding
import org.jetbrains.anko.support.v4.toast

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View{
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        emailEditText = binding.userEmail
        passwordEditText = binding.userPassword
        passwordConfirmEditText = binding.passwordConfirmation
        registerButton = binding.register
        loginTextView = binding.hasAccount
        emailEditText.hint = getString(R.string.enter_email, "")
        passwordEditText.hint = getString(R.string.enter_password, "")
        progressDialog = CustomProgressDialog(requireContext())

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
                        val email = emailEditText.text.toString()
                        val ref = FirebaseDatabase.getInstance().getReference("User")
                        val userId = ref.push().key
                        if (userId != null) {
                            val user = User(userId,email.split("@")[0],"Undefined","Undefined",email,"","")
                            ref.child(userId).setValue(user)
                        }
                        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                        if (sharedPref != null) {
                            with (sharedPref.edit()) {
                                putString("email", email)
                                apply()
                            }
                        }
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.RegisterFragmentToHomeFragment)
                    } else {
                        progressDialog.dismiss()
                        progressDialog.dismiss()
                        val snackBar = Snackbar.make(binding.root,
                            getString(R.string.already_exist, " "), 3000)
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
        }

        binding.register.setOnFocusChangeListener { view, b ->
            //hideKeyBoard()
            toast("hi")
        }
        passwordConfirmEditText.nextFocusDownId = R.id.register

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