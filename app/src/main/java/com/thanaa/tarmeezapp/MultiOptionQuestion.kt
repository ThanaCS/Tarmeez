package com.thanaa.tarmeezapp

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thanaa.tarmeezapp.data.User
import com.thanaa.tarmeezapp.databinding.FragmentMultiOptionQuestionBinding
import org.jetbrains.anko.support.v4.toast


class MultiOptionQuestion : Fragment() {
    private lateinit var questionTextView:TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var optionOneRadioButton: RadioButton
    private lateinit var optionTwoRadioButton: RadioButton
    private lateinit var optionThreeRadioButton: RadioButton
    private lateinit var moveToSectionsButton:TextView
    private lateinit var scoresTextView:TextView
    private lateinit var  binding  : FragmentMultiOptionQuestionBinding
    private var currentIndex =0
    private val args:MultiOptionQuestionArgs by navArgs()
    private var questions:ArrayList<String> = ArrayList()
    private var options:ArrayList<String> = ArrayList()
    private var answers:ArrayList<String> = ArrayList()
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var preferencesProvider: PreferencesProvider
    val KEY_USER = "User"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding  = FragmentMultiOptionQuestionBinding.inflate(inflater, container,
            false)

        preferencesProvider = PreferencesProvider(requireContext())
        val user = preferencesProvider.getUser(KEY_USER)

        binding.username.setText(user.username)

        val gender = user.gender

        if(gender.equals("ذكر")){
            binding.profileImage.setImageDrawable(resources.getDrawable(R.drawable.boy_avatar))
        }else if(gender.equals("أنثى")){
            binding.profileImage.setImageDrawable(resources.getDrawable(R.drawable.girl_avatar))
        }

        questionTextView = binding.question
        radioGroup = binding.answer
        optionOneRadioButton =   binding.optionOne
        optionTwoRadioButton =   binding.optionTwo
        optionThreeRadioButton =   binding.optionThree
        moveToSectionsButton = binding.moveToSections
        scoresTextView = binding.score

        setScores(user)
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
                            updateScores(user)
                            popUpCoin()
                            disableOrEnableRGButton(radioGroup, false)
                            selectedAnswer.background = resources
                                .getDrawable(R.drawable.correct_style)
                        }else{
                            disableOrEnableRGButton(radioGroup, false)
                            controlSound(R.raw.incorrect_sound_effect)
                            selectedAnswer.background = resources
                                .getDrawable(R.drawable.incorrect_style)
                            moveToSectionsButton.text = "مراجعة المحتوى"
                            moveToSectionsButton.setOnClickListener {
                                Navigation.findNavController(binding.root).popBackStack()
                            }
                        }
                    }

                    moveToSectionsButton.setOnClickListener {
                        val action = MultiOptionQuestionDirections.
                        MultiOptionQuestionToSectionsFragment(args.planetId)
                        Navigation.findNavController(binding.root).
                        navigate(action)
                    }
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


    private fun controlSound(soundId:Int){
        mediaPlayer = MediaPlayer.create(requireContext(), soundId)
        mediaPlayer.start()

    }

    private fun updateScores(user: User){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val email = sharedPref?.getString("email","email")
        if (email != null){
            FirebaseDatabase.getInstance().reference
                .child("User").orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val score = it.child("score").value.toString().toInt()
                            val userId = it.child("userId").value.toString()
                            val updatedScores = score + 20
                            val user = User(user.userId, user.username, user.age, user.gender, user.email, updatedScores, user.flag)
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

    private fun setScores(user: User){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val email = sharedPref?.getString("email","email")
        if (email != null){
            FirebaseDatabase.getInstance().reference
                .child("User").orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val score = it.child("score").value.toString()
                            scoresTextView.text = score
                            println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1 $score")
                            val user = User(user.userId, user.username, user.age, user.gender, user.email, score.toInt(), user.flag)
                            preferencesProvider.putUser(KEY_USER, user)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
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