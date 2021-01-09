package com.thanaa.tarmeezapp

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.marginEnd
import androidx.core.view.setMargins
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.thanaa.tarmeezapp.databinding.FragmentDragAndDropQuizBinding
import com.thanaa.tarmeezapp.databinding.FragmentDragAndDropTestBinding
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.textColor
import java.util.*

class DragAndDropQuizFragment : Fragment() {
    private val args by navArgs<DragAndDropQuizFragmentArgs>()

    private lateinit var binding : FragmentDragAndDropTestBinding
    private lateinit var questionButton: Button
    private lateinit var answerButton: Button
    private lateinit var moveToSection:TextView
    private lateinit var mediaPlayer: MediaPlayer
    private var numOfQuestions = 0
    private var numOfAnswers =  0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDragAndDropTestBinding.inflate(inflater, container, false)
        binding.question.text = args.question
        moveToSection = binding.moveToSections
        moveToSection.setOnClickListener {
            val action = DragAndDropQuizFragmentDirections.
            DragAndDropQuizFragmentToSectionsFragment(args.planetId)
            Navigation.findNavController(binding.root).navigate(action)
        }

//        val text = "22,'ك',\"ازرق\",8.4"
//        val answer = "String=\"ازرق\",Double=8.4,Char='ك',Int=22"

//        val text = "==,>,!="
//        val answer = "  7                            5  *>,  9                            9  *==, "ساره"               "لمياء" *!="

//        7,_______,5*9,_______,9*"ساره",_______,"لمياء"

        val map = args.answer.split(",").associate {
            val (left, right) = it.split("*")
            right to left
        }
        val list = args.options.split(",")
        numOfQuestions = list.size
        for (i in list.indices){
            questionButton = Button(requireContext())
            questionButton.layoutParams = ViewGroup.LayoutParams(
                200,
                100)
            questionButton.text = list[i]
            binding.questionLinearLayout.addView(questionButton)
            questionButton.textColor = resources.getColor(R.color.white)
            questionButton.background = resources.getDrawable(R.drawable.yellow_button_style)
            questionButton.setBackgroundColor(resources.getColor(R.color.pink_1))
            questionButton.setPadding(0,0,0,5)
            questionButton.setOnLongClickListener {
                setDragListener(map,list[i])
                val clipText = "أحسنت ياصغيري"
                val item = ClipData.Item(clipText)
                val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
                val data = ClipData(clipText,mimeTypes, item)
                val dragShadowBuilder = View.DragShadowBuilder(it)
                it.startDragAndDrop(data,dragShadowBuilder, it , 0)
                it.visibility = View.INVISIBLE
                true
            }

        }


        for (i in map){
            answerButton = Button(requireContext())
            answerButton.layoutParams = ViewGroup.LayoutParams(
                200, 100)
            answerButton.text = i.value
            answerButton.textColor = resources.getColor(R.color.white)
            answerButton.setBackgroundColor(resources.getColor(R.color.cyan))
            answerButton.setPadding(0,0,0,5)
            binding.answerLinearLayout.addView(answerButton)
        }


        return binding.root
    }

    private fun setDragListener(map : Map<String,String>, question :String){
        binding.answerLinearLayout.forEach {
            val view  = it as Button
            if(view.text == map[question]){
                view.setOnDragListener(dragListener)
            }else{
                view.setOnDragListener(wrongDragListener)
            }
        }
    }

    private val dragListener = View.OnDragListener { view, event ->
        when(event.action){
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                view.setBackgroundColor(resources.getColor(R.color.dark_green))
                view.setPadding(0,0,0,5)
                view.invalidate()
                val v = event.localState as View
                val owner =  v.parent as ViewGroup
                owner.removeView(v)
                v.visibility = View.VISIBLE
                numOfAnswers += 1
                if(numOfQuestions== numOfAnswers){
                    binding.questionLinearLayout.visibility = View.GONE
                    val layoutParams= LinearLayout
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT)
                    layoutParams.setMargins(0, 0, 0, 0)
                    layoutParams.gravity = Gravity.CENTER
                    binding.answerLinearLayout.layoutParams = layoutParams
                    controlSound(R.raw.correct_sound_effect)
                }
                true

            }
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()
                val v = event.localState as View
                v.visibility = View.VISIBLE
                true
            }
            else -> false

        }
    }

    private val wrongDragListener = View.OnDragListener { view, event ->
        false
    }

    private fun controlSound(soundId:Int) {
        mediaPlayer = MediaPlayer.create(requireContext(), soundId)
        mediaPlayer.start()
    }
}