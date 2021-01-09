package com.thanaa.tarmeezapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.thanaa.tarmeezapp.databinding.FragmentForgotPasswordBinding
import com.thanaa.tarmeezapp.databinding.FragmentLoginBinding

class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var aAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        binding.forgotPassword.setOnClickListener {

            aAuth = FirebaseAuth.getInstance()
            val email = binding.resetPassword.text.toString()

            if(email.isEmpty()){
                Toast.makeText(requireContext(), "فضلاً أدخل البريد الإلكتروني", Toast.LENGTH_SHORT).show()
            }else{
                aAuth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "تم إرسال البريد الإلكتروني", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }

        return binding.root
    }

}