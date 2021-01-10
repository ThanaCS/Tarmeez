package com.thanaa.tarmeezapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.github.matteobattilana.weather.PrecipType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.thanaa.tarmeezapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var index: Int = 0
    private var aAuth: FirebaseAuth
    private var user: FirebaseUser
    private val planetList = mutableListOf<LottieAnimationView>()

    init {
        aAuth = FirebaseAuth.getInstance()
        user = aAuth.currentUser!!
        val email = user.email
        FirebaseDatabase.getInstance().reference
            .child("User").orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val username = it.child("username").value.toString()
                        binding.username.text = username
                    }

                }

            })
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        //val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        //val email = sharedPref?.getString("email","email")
        planetList.addAll(listOf(binding.variablesEarth,binding.relationsConditionsMars))

        checkFlag()

        starsAnimation()


        binding.variablesEarth.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSectionsFragment("-MQGx5xPpOMcFnKbkiXO")
            findNavController().navigate(action)
        }

        binding.relationsConditionsMars.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSectionsFragment("-MQNm_koTjLrLtWvswhW")
            findNavController().navigate(action)
        }

        showNavigation()
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

    private fun showNavigation() {
        val bottomNavigationView = (activity as MainActivity).bottomNavigationView
        val fab = (activity as MainActivity).fab
        val bottomAppBar = (activity as MainActivity).bottomAppBar
        bottomNavigationView.visibility = View.VISIBLE
        bottomAppBar.visibility = View.VISIBLE
        fab.visibility = View.VISIBLE
    }


    private fun checkFlag(){
        FirebaseDatabase.getInstance().reference
            .child("Planet")
            .addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val flag = it.child("flag").value.toString()
                        if(flag == "0"){
                            planetList[index].visibility = View.INVISIBLE
                        }else{
                            planetList[index].visibility = View.VISIBLE

                        }
                        index++
                        }
                    }
                })

    }
}