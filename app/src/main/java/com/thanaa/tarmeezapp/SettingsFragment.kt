package com.thanaa.tarmeezapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thanaa.tarmeezapp.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var key: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val email = sharedPref?.getString("email","email")
        if (email != null){
            FirebaseDatabase.getInstance().reference
                .child("User").orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            key = it.child("userId").value.toString()
                            binding.ageEditText.setText(it.child("age").value.toString())
                            binding.usernameEditText.setText(it.child("username").value.toString())
                            val gender = it.child("gender").value.toString()
                            if(gender == "أنثى"){
                                binding.girlImageView.borderWidth = 10
                                binding.boyImageView.borderWidth = 0
                            } else if(gender == "ذكر"){
                                binding.girlImageView.borderWidth = 0
                                binding.boyImageView.borderWidth = 10
                            }else{
                                binding.girlImageView.borderWidth = 0
                                binding.boyImageView.borderWidth = 0
                            }
                        }

                    }

                })
        }

        binding.editUsername.setOnClickListener {
            binding.usernameEditText.isEnabled = binding.usernameEditText.isEnabled != true
        }

        binding.editAge.setOnClickListener {
            binding.ageEditText.isEnabled = binding.ageEditText.isEnabled != true
        }

        binding.girlImageView.setOnClickListener {
            binding.girlImageView.borderWidth = 10
            binding.boyImageView.borderWidth = 0
            binding.genderTextView.text = "أنثى"
        }

        binding.boyImageView.setOnClickListener {
            binding.girlImageView.borderWidth = 0
            binding.boyImageView.borderWidth = 10
            binding.genderTextView.text = "ذكر"

        }

        binding.saveButton.setOnClickListener {

            val hashMap = mutableMapOf<String,Any>()
            hashMap["age"] = binding.ageEditText.text.toString()
            hashMap["username"] = binding.usernameEditText.text.toString()
            hashMap["gender"] = binding.genderTextView.text.toString()

            FirebaseDatabase.getInstance().reference
                .child("User").child(key).updateChildren(hashMap).addOnSuccessListener {
                    Toast.makeText(requireContext(),"Success", Toast.LENGTH_SHORT).show()
                    binding.usernameEditText.isEnabled = false
                    binding.ageEditText.isEnabled = false
                }
        }

        return binding.root
    }
}