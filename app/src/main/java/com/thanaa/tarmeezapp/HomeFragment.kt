package com.thanaa.tarmeezapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.matteobattilana.weather.PrecipType
import com.google.firebase.database.*
import com.thanaa.tarmeezapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var key: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val email = sharedPref?.getString("email","email")
        FirebaseDatabase.getInstance().reference
            .child("Planet")
            .addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                     key = it.key
                    }
                }

            })


        FirebaseDatabase.getInstance().reference
            .child("User").orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        Log.d("TAG", "onDataChange:********************************************************* $it")
                        val username = it.child("username").value.toString()
                        binding.username.text = username
                    }

                }

            })
        starsAnimation()


        binding.variablesEarth.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSectionsFragment("-MQGx5xPpOMcFnKbkiXO")
            findNavController().navigate(action)
        }

        binding.relationsConditionsMars.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSectionsFragment("-MQNm_koTjLrLtWvswhW")
            findNavController().navigate(action)
        }

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