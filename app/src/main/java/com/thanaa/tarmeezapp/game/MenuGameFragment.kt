package com.thanaa.tarmeezapp.game

import android.app.Activity
import android.graphics.Point
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.thanaa.tarmeezapp.MainActivity
import com.thanaa.tarmeezapp.databinding.FragmentMenuGameBinding


class MenuGameFragment : Fragment() {
    private var _binding:FragmentMenuGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var gameView: GameView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        hideNavigation()
        _binding = FragmentMenuGameBinding.inflate(inflater, container, false)
        var point = Point()
        (context as Activity?)!!.windowManager
            .defaultDisplay.getSize(point)
        gameView = GameView(requireContext(),point.x,point.y)
        binding.play.setOnClickListener{
            val action = MenuGameFragmentDirections.actionMenuGameFragmentToGameFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
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