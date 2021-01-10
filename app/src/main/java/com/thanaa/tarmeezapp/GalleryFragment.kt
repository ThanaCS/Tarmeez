package com.thanaa.tarmeezapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.thanaa.tarmeezapp.databinding.FragmentGalleryBinding
import com.thanaa.tarmeezapp.databinding.FragmentMenuGameBinding


class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        binding.alienImage.setOnClickListener{
            val action = GalleryFragmentDirections.actionGalleryFragmentToMenuGameFragment()
            findNavController().navigate(action)
        }

        showNavigation()
        return binding.root
    }

    private fun showNavigation() {
        val bottomNavigationView = (activity as MainActivity).bottomNavigationView
        val fab = (activity as MainActivity).fab
        val bottomAppBar = (activity as MainActivity).bottomAppBar
        bottomNavigationView.visibility = View.VISIBLE
        bottomAppBar.visibility = View.VISIBLE
        fab.visibility = View.VISIBLE
    }
}