package com.thanaa.tarmeezapp

import android.animation.Animator
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.forEach
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thanaa.tarmeezapp.databinding.FragmentFinalGameBinding


class FinalGameFragment : Fragment() {
    private val args by navArgs<FinalGameFragmentArgs>()
    private var _binding: FragmentFinalGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var questionTextView: TextView
    private val listOfButton = mutableListOf<TextView>()
    private lateinit var options: String
    private lateinit var answer: String
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinalGameBinding.inflate(inflater, container, false)

        FirebaseDatabase.getInstance().reference
            .child("Game").child(args.gameId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    options = snapshot.child("options").value.toString()
                    answer = snapshot.child("answer").value.toString()
                    setData()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

//        val options = "},if(alien.isClose()),{,score++,shoot()"
//        val answer = ",if(alien.isClose()),{,shoot(),score++,}"

        binding.questionTextView.text = "برمج اللعبة:"

        binding.doneButton.setOnClickListener {
            var sentence = ""
            binding.answerLinearLayout.forEach {
                val view = it as TextView
                sentence += ","+view.text
            }
            if(sentence.equals(answer)){
                controlSound(R.raw.correct_sound_effect)
                popUpCoin()
            }else{
                controlSound(R.raw.incorrect_sound_effect)
                for(j in listOfButton.indices){
                    if(listOfButton[j].parent == binding.answerLinearLayout){
                        binding.answerLinearLayout.removeView(listOfButton[j])
                        binding.questionLinearLayout.addView(listOfButton[j])
                    }
                }

            }
        }

        return binding.root
    }
    private fun controlSound(soundId:Int) {
        mediaPlayer = MediaPlayer.create(requireContext(), soundId)
        mediaPlayer.start()
    }
    private fun popUpCoin(){
        binding.popupCoin.visibility = View.VISIBLE
        binding.popupCoin.playAnimation()
        binding.popupCoin.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                binding.popupCoin.visibility = View.GONE

            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })

    }

    private fun setData(){
        val list = options.split(",")

        for (i in list.indices){
            questionTextView = TextView(requireContext())
            questionTextView.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            val layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            questionTextView.setPadding(20,20, 20, 20)
            questionTextView.layoutParams = layoutParams
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
    }

}