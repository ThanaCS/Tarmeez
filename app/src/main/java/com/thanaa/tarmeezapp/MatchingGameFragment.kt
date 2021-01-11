package com.thanaa.tarmeezapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.thanaa.tarmeezapp.databinding.FragmentMatchingGameBinding
import org.jetbrains.anko.support.v4.toast
import java.util.*
import kotlin.collections.ArrayList


class MatchingGameFragment : Fragment() {
    private var _binding: FragmentMatchingGameBinding? = null
    private val binding get() = _binding!!
    private var numOfButtons = 0
    private var results:ArrayList<Int> = ArrayList()
    val handler: Handler = Handler()
    private lateinit var mediaPlayer: MediaPlayer
    private var numOfMatching = 0
    private val tags = listOf("heart", "astronaut", "rocket", "heart", "astronaut", "rocket")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentMatchingGameBinding.inflate(inflater, container, false)
        setRandomTagsToTheButtons()
        setImageButtonsOnClickListener()
        return binding.root
    }

    private fun setImageButtonsOnClickListener(){
        //row one
        binding.bu1.setOnClickListener {
            if(numOfButtons < 2){
                binding.bu1.apply {
                    setImageButtonDrawable(tag.toString(), id)
                }
                results.add(binding.bu1.id)
                numOfButtons ++
                if(numOfButtons ==2){
                    checkIfButtonsMatch()
                }
            }
        }

        binding.bu2.setOnClickListener {
            if(numOfButtons < 2){
                binding.bu2.apply {
                    setImageButtonDrawable(tag.toString(), id)
                }
                results.add(binding.bu2.id)
                numOfButtons ++
                if(numOfButtons ==2){
                    checkIfButtonsMatch()
                }

            }
        }

        binding.bu3.setOnClickListener {
            if(numOfButtons < 2){
                binding.bu3.apply {
                    setImageButtonDrawable(tag.toString(), id)
                }
                results.add(binding.bu3.id)
                numOfButtons ++
                if(numOfButtons == 2){
                    checkIfButtonsMatch()
                }
            }
        }

        //row two
        binding.bu4.setOnClickListener {
            if(numOfButtons < 2){
                binding.bu4.apply {
                    setImageButtonDrawable(tag.toString(), id)
                }
                results.add(binding.bu4.id)
                numOfButtons ++
                if(numOfButtons == 2){
                    checkIfButtonsMatch()
                }
            }
        }

        binding.bu5.setOnClickListener {
            if(numOfButtons < 2){
                binding.bu5.apply {
                    setImageButtonDrawable(tag.toString(), id)
                }
                results.add(binding.bu5.id)
                numOfButtons ++
                if(numOfButtons == 2){
                    checkIfButtonsMatch()
                }
            }
        }

        binding.bu6.setOnClickListener {
            if(numOfButtons < 2){
                binding.bu6.apply {
                    setImageButtonDrawable(tag.toString(), id)
                }
                results.add(binding.bu6.id)
                numOfButtons ++
                if(numOfButtons == 2){
                    checkIfButtonsMatch()
                }
            }
        }
    }

    private fun checkIfButtonsMatch(){
        if(binding.root.findViewById<ImageView>(results[0]).tag.toString() ==
            binding.root.findViewById<ImageView>(results[1]).tag.toString()){
            toast("متطابقة")
            results = ArrayList()
            numOfButtons = 0
            numOfMatching ++
            if(numOfMatching == 3){
                controlSound(R.raw.correct_sound_effect)
            }
        }else{
            toast("غير متطابقة")
            handler.postDelayed({
                binding.root.findViewById<ImageView>(results[0]).apply {
                    setImageDrawable(resources.getDrawable(R.drawable.matching_image_question))
                    isEnabled = true
                }
                binding.root.findViewById<ImageView>(results[1]).apply {
                    setImageDrawable(resources.getDrawable(R.drawable.matching_image_question))
                    isEnabled = true
                }
                results = ArrayList()
                numOfButtons = 0
            }, 1500)
        }
    }

    private fun controlSound(soundId:Int) {
        mediaPlayer = MediaPlayer.create(requireContext(), soundId)
        mediaPlayer.start()
    }

    private fun setImageButtonDrawable(tag:String, id:Int){
        when(tag.trim()){
            "heart" ->{
                binding.root.findViewById<ImageView>(id).apply {
                    setImageDrawable(resources.getDrawable(R.drawable.heart))
                    isEnabled = false
                }
            }

           "rocket"->{
               binding.root.findViewById<ImageView>(id).apply {
                   setImageDrawable(resources.getDrawable(R.drawable.rocket))
                   isEnabled = false
               }
           }

           "astronaut"->{
               binding.root.findViewById<ImageView>(id).apply {
                   setImageDrawable(resources.getDrawable(R.drawable.astronaut))
                   isEnabled = false
               }
           }
        }
    }

    private fun setRandomTagsToTheButtons(){
        Collections.shuffle(tags)
        binding.bu1.tag = tags[0]
        binding.bu2.tag = tags[1]
        binding.bu3.tag = tags[2]
        binding.bu4.tag = tags[3]
        binding.bu5.tag = tags[4]
        binding.bu6.tag = tags[5]
    }
}