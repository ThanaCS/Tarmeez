package com.thanaa.tarmeezapp.game

import android.app.Activity
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thanaa.tarmeezapp.MainActivity
import org.jetbrains.anko.displayManager


class GameFragment : Fragment() {

    private lateinit var gameView: GameView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        hideNavigation()

        var point = Point()
        (context as Activity?)!!.windowManager
            .defaultDisplay.getSize(point)
        gameView = GameView(this,point.x,point.y)



        return gameView
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