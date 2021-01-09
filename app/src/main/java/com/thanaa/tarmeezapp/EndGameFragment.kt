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
        return binding.root
    }


}