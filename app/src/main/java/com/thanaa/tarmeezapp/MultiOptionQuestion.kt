package com.thanaa.tarmeezapp

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thanaa.tarmeezapp.databinding.FragmentMultiOptionQuestionBinding


class MultiOptionQuestion : Fragment() {
    private lateinit var questionNumberTextView:TextView
    private lateinit var questionTextView:TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var optionOneRadioButton: RadioButton
    private lateinit var optionTwoRadioButton: RadioButton
    private lateinit var optionThreeRadioButton: RadioButton
    private lateinit var nextButton: ImageView
    private lateinit var doneButton:ImageView
    private var currentIndex =0
    private val args:MultiOptionQuestionArgs by navArgs()
    private var questions:ArrayList<String> = ArrayList()
    private var options:ArrayList<String> = ArrayList()
    private var answers:ArrayList<String> = ArrayList()
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        val binding  = FragmentMultiOptionQuestionBinding.inflate(inflater, container,
            false)
        questionTextView = binding.question
        radioGroup = binding.answer
        nextButton = binding.nextButton
        doneButton = binding.doneButton
        optionOneRadioButton =   binding.optionOne
        optionTwoRadioButton =   binding.optionTwo
        optionThreeRadioButton =   binding.optionThree

        FirebaseDatabase.getInstance().reference
            .child("Planet")
            .child(args.planetId)
            .child("section")
            .child(args.sectionId.toString())
            .child("content")
            .child("0").child("quiz").
            addListenerForSingleValueEvent(object : ValueEventListener {

                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        questions.add(it.child("question").value.toString())
                        options.add(it.child("options").value.toString())
                        answers.add(it.child("answer").value.toString())
                    }

                    questionTextView.text = Html.fromHtml(questions[currentIndex],0)
                    toast(answers[currentIndex])

                    val answerOptions = options[currentIndex].split(",")
                    optionOneRadioButton.text = answerOptions[0]
                    optionTwoRadioButton.text = answerOptions[1]
                    optionThreeRadioButton.text = answerOptions[2]

                    radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                        val selectedAnswer:RadioButton =radioGroup.findViewById(radioGroup
                            .checkedRadioButtonId)
                        val answer = selectedAnswer.text
                        if (answer.trim() == answers[currentIndex].trim()) {
                            controlSound(R.raw.correct_sound_effect)
                            disableOrEnableRGButton(radioGroup, false)
                            selectedAnswer.background = resources
                                .getDrawable(R.drawable.correct_style)
                        }else{
                            disableOrEnableRGButton(radioGroup, false)
                            selectedAnswer.background = resources
                                .getDrawable(R.drawable.incorrect_style)
                        }
                    }

                    nextButton.setOnClickListener {
                        moveToNext()
                    }
                    doneButton.setOnClickListener {
                        Navigation.findNavController(binding.root).popBackStack()
                    }
                    checkIfUserFinish()

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        return binding.root
    }
    private fun disableOrEnableRGButton(radioGroup: RadioGroup, enable_or_disable: Boolean) {
        for (i in 0 until radioGroup.childCount) {
            (radioGroup.getChildAt(i) as RadioButton).isEnabled = enable_or_disable
        }
    }

    fun moveToNext(){
        currentIndex += 1
        if(currentIndex < questions.size){
            questionTextView.text = questions[currentIndex]
            val answerOptions = options[currentIndex].split(",")
            optionOneRadioButton.text = answerOptions[0]
            optionTwoRadioButton.text = answerOptions[1]
            optionThreeRadioButton.text = answerOptions[2]
            checkIfUserFinish()
        }
    }

    fun checkIfUserFinish(){
        if (currentIndex + 1 == questions.size) {
            nextButton.visibility = View.GONE
            doneButton.visibility = View.VISIBLE
        } else {
            nextButton.visibility = View.VISIBLE
            doneButton.visibility = View.GONE
        }
    }

    private fun controlSound(soundId:Int){
        mediaPlayer = MediaPlayer.create(requireContext(), soundId)
        mediaPlayer.start()

    }
}