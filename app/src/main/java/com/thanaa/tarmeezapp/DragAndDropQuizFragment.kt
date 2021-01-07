package com.thanaa.tarmeezapp

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.os.Bundle
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.forEach
import androidx.navigation.fragment.navArgs
import com.thanaa.tarmeezapp.databinding.FragmentDragAndDropQuizBinding
import java.util.*

class DragAndDropQuizFragment : Fragment() {
    private val args by navArgs<DragAndDropQuizFragmentArgs>()

    private lateinit var binding : FragmentDragAndDropQuizBinding
    private lateinit var questionButton: Button
    private lateinit var answerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDragAndDropQuizBinding.inflate(inflater, container, false)

        binding.question.text = args.question

//        val text = "22,'ك',\"ازرق\",8.4"
//        val answer = "String=\"ازرق\",Double=8.4,Char='ك',Int=22"

        val map = args.answer.split(",").associate {
            val (left, right) = it.split("=")
            right to left
        }
        val list = args.options.split(",")

        for (i in list.indices){
            questionButton = Button(requireContext())
            questionButton.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            questionButton.text = list[i]
            binding.questionLinearLayout.addView(questionButton)
            questionButton.setBackgroundColor(resources.getColor(R.color.pink_1))
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
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            answerButton.text = i.value
            answerButton.setBackgroundColor(resources.getColor(R.color.cyan))

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
                val item = event.clipData.getItemAt(0)
                val dragData = item.text
                Toast.makeText(requireContext(), dragData, Toast.LENGTH_LONG).show()
                view.setBackgroundColor(resources.getColor(R.color.pink))
                view.invalidate()
                val v = event.localState as View
                val owner =  v.parent as ViewGroup
                owner.removeView(v)
                v.visibility = View.VISIBLE
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
}