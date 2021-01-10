package com.thanaa.tarmeezapp

import android.animation.Animator
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thanaa.tarmeezapp.data.User
import com.thanaa.tarmeezapp.databinding.FragmentDragAndDropQuizBinding
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.textColor
import java.util.*

class DragAndDropQuizFragment : Fragment() {
    private val args by navArgs<DragAndDropQuizFragmentArgs>()

    private lateinit var binding : FragmentDragAndDropQuizBinding
    private lateinit var questionButton: Button
    private lateinit var answerButton: Button
    private lateinit var moveToSection:TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var scoresTextView:TextView
    private var numOfQuestions = 0
    private var numOfAnswers =  0

    private lateinit var preferencesProvider: PreferencesProvider
    val KEY_USER = "User"

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDragAndDropQuizBinding.inflate(inflater, container, false)
        binding.question.text = args.question
        moveToSection = binding.moveToSections
        scoresTextView = binding.score

        preferencesProvider = PreferencesProvider(requireContext())
        user = preferencesProvider.getUser(KEY_USER)

        binding.username.setText(user.username)

        val gender = user.gender

        if(gender.equals("ذكر")){
            binding.profileImage.setImageDrawable(resources.getDrawable(R.drawable.boy_avatar))
        }else if(gender.equals("أنثى")){
            binding.profileImage.setImageDrawable(resources.getDrawable(R.drawable.girl_avatar))
        }

        setScores()
        moveToSection.setOnClickListener {
            val action = DragAndDropQuizFragmentDirections.
            DragAndDropQuizFragmentToSectionsFragment(args.planetId)
            Navigation.findNavController(binding.root).navigate(action)
        }

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
            questionButton.textSize = 13F
            questionButton.background = resources.getDrawable(R.drawable.word_layout)
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
            answerButton.textSize = 13F
            answerButton.textColor = resources.getColor(R.color.white)
            answerButton.background = resources.getDrawable(R.drawable.word_layout2)
            answerButton.setPadding(0,0,0,5)
            binding.answerLinearLayout.addView(answerButton)
        }


        showNavigation()
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
                view.background = resources.getDrawable(R.drawable.word_layout1)
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
                    updateScores()
                    popUpCoin()
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
    private fun updateScores(){
        val email = user.email
        if (email != null){
            FirebaseDatabase.getInstance().reference
                .child("User").orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val score = it.child("score").value.toString().toInt()
                            val userId = it.child("userId").value.toString()
                            val updatedScores = score + 20
                             user = User(user.userId, user.username, user.age, user.gender, user.email, updatedScores, user.flag)
                            preferencesProvider.putUser(KEY_USER, user)
                            FirebaseDatabase.getInstance().reference.child("User")
                                .child(userId)
                                .child("score").setValue(updatedScores)
                            FirebaseDatabase.getInstance().reference
                            scoresTextView.text = updatedScores.toString()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    private fun setScores(){
        val email = user.email
        if (email != null){
            FirebaseDatabase.getInstance().reference
                .child("User").orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val score = it.child("score").value.toString()
                            scoresTextView.text = score
                            user = User(user.userId, user.username, user.age, user.gender, user.email, score.toInt(), user.flag)
                            preferencesProvider.putUser(KEY_USER, user)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    private fun showNavigation() {
        val bottomNavigationView = (activity as MainActivity).bottomNavigationView
        val fab = (activity as MainActivity).fab
        val bottomAppBar = (activity as MainActivity).bottomAppBar
        bottomNavigationView.visibility = View.VISIBLE
        bottomAppBar.visibility = View.VISIBLE
        fab.visibility = View.VISIBLE
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
}