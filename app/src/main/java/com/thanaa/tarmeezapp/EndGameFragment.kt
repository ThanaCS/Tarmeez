package com.thanaa.tarmeezapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.thanaa.tarmeezapp.databinding.FragmentEndGameBinding
import com.thanaa.tarmeezapp.game.MenuGameFragmentDirections

class EndGameFragment : Fragment() {
    private var _binding: FragmentEndGameBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEndGameBinding.inflate(inflater, container, false)

        binding.restrt.setOnClickListener {
            val action = EndGameFragmentDirections.actionEndGameFragment2ToMenuGameFragment()
            findNavController().navigate(action)
        }
        binding.next.setOnClickListener {
            val action = EndGameFragmentDirections.actionEndGameFraagmentToFinalGameFragment()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}