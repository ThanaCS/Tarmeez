package com.thanaa.tarmeezapp

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat.setBackgroundTintList
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thanaa.tarmeezapp.data.User
import com.thanaa.tarmeezapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var aAuth:FirebaseAuth
    private lateinit var preferencesProvider: PreferencesProvider
    val KEY_USER = "User"
    private lateinit var key: String
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        preferencesProvider = PreferencesProvider(requireContext())

        val user = preferencesProvider.getUser(KEY_USER)

        email = user.email

        binding.emailEditText.setText(email)

        val gender = user.gender

        if(gender.equals("ذكر")){
            binding.avatarContainer.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.cyan)));
            binding.avatar.setImageDrawable(resources.getDrawable(R.drawable.boy))
        }else if(gender.equals("أنثى")){
            binding.avatarContainer.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.pink_1)));
            binding.avatar.setImageDrawable(resources.getDrawable(R.drawable.girl))
        }

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

        binding.logoutIcon.setOnClickListener {
            aAuth = FirebaseAuth.getInstance()
            aAuth.signOut()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
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

            val age =  binding.ageEditText.text.toString()
            val userName = binding.usernameEditText.text.toString()
            val gender = binding.genderTextView.text.toString()

            binding.usernameEditText.setText(userName)
            binding.ageEditText.setText(age)

            if(gender.equals("ذكر")){
                binding.avatarContainer.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.cyan)));
                binding.avatar.setImageDrawable(resources.getDrawable(R.drawable.boy))
            }else if(gender.equals("أنثى")){
                binding.avatarContainer.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.pink_1)));
                binding.avatar.setImageDrawable(resources.getDrawable(R.drawable.girl))
            }

            val hashMap = mutableMapOf<String,Any>()
            hashMap["age"] = age
            hashMap["username"] = userName
            hashMap["gender"] = gender

            FirebaseDatabase.getInstance().reference
                .child("User").child(key).updateChildren(hashMap).addOnSuccessListener {
                    Toast.makeText(requireContext(),"Success", Toast.LENGTH_SHORT).show()
                    binding.usernameEditText.isEnabled = false
                    binding.ageEditText.isEnabled = false
                }

            val userObject = User(key, userName, age, gender, email)
            preferencesProvider.putUser(KEY_USER, userObject)
        }

        return binding.root
    }

}