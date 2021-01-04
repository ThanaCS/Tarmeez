package com.thanaa.tarmeezapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.thanaa.tarmeezapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        hideNavigation()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.login.setOnClickListener {
            Toast.makeText(requireContext(), "Hi", Toast.LENGTH_LONG).show()
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
}