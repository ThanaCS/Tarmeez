package com.thanaa.tarmeezapp

import android.media.Image
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.github.matteobattilana.weather.PrecipType
import com.google.firebase.database.*
import com.thanaa.tarmeezapp.data.Planet
import com.thanaa.tarmeezapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var ref: DatabaseReference
    lateinit var planetList: MutableList<Planet>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

//        planetList = mutableListOf()
        FirebaseDatabase.getInstance().reference
            .child("Planet")
            .child("-MQGV728WSURI5evrtVd")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var v = snapshot.value
                    println("########################################################################### ${v}")
                }

            })

        starsAnimation()


//        ref.addValueEventListener(object : ValueEventListener{
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()){
//                    for(i in snapshot.children){
//                        val planet = i.getValue(Planet::class.java)
//                        planetList.add(planet!!)
//                    }
//                }
//                println("######################################################3 ${planetList}")
//            }
//
//        })

        binding.variablesEarth.setOnClickListener {

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