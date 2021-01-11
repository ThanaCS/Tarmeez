package com.thanaa.tarmeezapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.thanaa.tarmeezapp.data.User
import com.thanaa.tarmeezapp.databinding.FragmentGalleryBinding
import com.thanaa.tarmeezapp.databinding.FragmentMenuGameBinding


class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferencesProvider: PreferencesProvider
    val KEY_USER = "User"

    private lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        preferencesProvider = PreferencesProvider(requireContext())
        user = preferencesProvider.getUser(KEY_USER)

        binding.username.setText(user.username)
        binding.score.setText(user.score.toString())

        val gender = user.gender

        if(gender.equals("ذكر")){
            binding.profileImage.setImageDrawable(resources.getDrawable(R.drawable.boy_avatar))
        }else if(gender.equals("أنثى")){
            binding.profileImage.setImageDrawable(resources.getDrawable(R.drawable.girl_avatar))
        }


        binding.alienImage.setOnClickListener{
            val action = GalleryFragmentDirections.actionGalleryFragmentToMenuGameFragment()
            findNavController().navigate(action)
        }

        binding.cardImage.setOnClickListener{
            val action = GalleryFragmentDirections.actionGalleryFragmentToMatchingGameFragment()
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