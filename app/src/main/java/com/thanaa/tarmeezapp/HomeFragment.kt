package com.thanaa.tarmeezapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.github.matteobattilana.weather.PrecipType
import com.thanaa.tarmeezapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        starsAnimation()
        return binding.root
    }

    private fun starsAnimation(){
        binding.weatherAnimation.apply {
            setWeatherData(PrecipType.RAIN)
            speed = 200
            angle = 45
            emissionRate = 120f

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}