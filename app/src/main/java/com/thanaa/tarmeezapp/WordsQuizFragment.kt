package com.thanaa.tarmeezapp

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.navigation.fragment.navArgs
import com.thanaa.tarmeezapp.databinding.FragmentDragAndDropQuizBinding
import com.thanaa.tarmeezapp.databinding.FragmentWordsQuizBinding
import java.util.*

class WordsQuizFragment : Fragment() {

    private val args by navArgs<WordsQuizFragmentArgs>()
    private lateinit var questionTextView: TextView
    private val listOfButton = mutableListOf<TextView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWordsQuizBinding.inflate(inflater, container, false)

        val list = args.options.split(",")

        binding.questionTextView.text = args.question

        for (i in list.indices){
            questionTextView = TextView(requireContext())
            questionTextView.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            questionTextView.layoutParams.width = 150
            questionTextView.layoutParams.height = 120
            questionTextView.gravity = Gravity.CENTER
            questionTextView.text = list[i]
            questionTextView.setTextColor(Color.WHITE)
            questionTextView.textSize = 18F
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
                }else{
                    owner.removeView(listOfButton[i])
                    binding.questionLinearLayout.addView(listOfButton[i])
                }

            }
        }

        binding.doneButton.setOnClickListener {
            var sentence = ""
            binding.answerLinearLayout.forEach {
                val view = it as TextView
                sentence += ","+view.text
            }
            if(sentence.equals(args.answer)){
                Toast.makeText(requireContext(),"correct", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"wrong", Toast.LENGTH_SHORT).show()

            }
        }

        return binding.root
    }


}