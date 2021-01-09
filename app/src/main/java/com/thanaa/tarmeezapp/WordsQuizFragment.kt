package com.thanaa.tarmeezapp

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.thanaa.tarmeezapp.databinding.FragmentDragAndDropQuizBinding
import com.thanaa.tarmeezapp.databinding.FragmentWordsQuizBinding
import org.jetbrains.anko.support.v4.toast
import java.util.*

class WordsQuizFragment : Fragment() {

    private val args by navArgs<WordsQuizFragmentArgs>()
    private lateinit var questionTextView: TextView
    private lateinit var moveToSection:TextView
    private val listOfButton = mutableListOf<TextView>()
    private var countNumOfWords = 0
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWordsQuizBinding.inflate(inflater, container, false)
        moveToSection = binding.moveToSections
        moveToSection.setOnClickListener {
            val action = WordsQuizFragmentDirections.
            WordsQuizFragmentToSectionsFragment(args.planetId)
            Navigation.findNavController(binding.root)
                .navigate(action)
        }
        val list = args.options.split(",")
        binding.questionTextView.text = args.question

        for (i in list.indices){
            questionTextView = TextView(requireContext())
            questionTextView.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            questionTextView.layoutParams.width = 170
            questionTextView.layoutParams.height = 120
            questionTextView.gravity = Gravity.CENTER
            questionTextView.text = list[i]
            questionTextView.setTextColor(Color.WHITE)
            questionTextView.textSize = 16F
            binding.questionLinearLayout.addView(questionTextView)
            questionTextView.background = resources.getDrawable(R.drawable.word_layout)
            listOfButton.add(i,questionTextView)
        }

        for (i in listOfButton.indices){
            listOfButton[i].setOnClickListener {
                val owner =  listOfButton[i].parent as ViewGroup
                if(listOfButton[i].parent == binding.questionLinearLayout){
                    owner.removeView(listOfButton[i])
                    binding.answerLinearLayout.addView(listOfButton[i])
                    countNumOfWords += 1
                    if(countNumOfWords == list.size){
                        var sentence = ""
                        binding.answerLinearLayout.forEach {
                            val view = it as TextView
                            sentence += ","+view.text
                        }
                        if(sentence.equals(args.answer)){
                            controlSound(R.raw.correct_sound_effect)
                        }else{
                            controlSound(R.raw.incorrect_sound_effect)
                            for(j in listOfButton.indices){
                                if(listOfButton[j].parent == binding.answerLinearLayout){
                                    binding.answerLinearLayout.removeView(listOfButton[j])
                                    binding.questionLinearLayout.addView(listOfButton[j])
                                }
                            }

                            countNumOfWords = 0
                        }
                    }
                }else{
                    owner.removeView(listOfButton[i])
                    binding.questionLinearLayout.addView(listOfButton[i])
                    countNumOfWords -=1
                }

            }
        }

        return binding.root
    }

    private fun controlSound(soundId:Int) {
        mediaPlayer = MediaPlayer.create(requireContext(), soundId)
        mediaPlayer.start()
    }

    }