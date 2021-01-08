package com.thanaa.tarmeezapp

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thanaa.tarmeezapp.databinding.FragmentContentBinding
import org.jetbrains.anko.support.v4.toast


class ContentFragment : Fragment() {

    val args by navArgs<ContentFragmentArgs>()
    lateinit var question : String
    lateinit var options : String
    lateinit var answer : String
    lateinit var type : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentContentBinding.inflate(inflater, container, false)

        FirebaseDatabase.getInstance().reference
            .child("Planet")
            .child(args.planetId.toString())
            .child("section")
            .child(args.sectionIndex.toString())
            .child("content")
            .orderByChild(args.contentTitle)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                        val title = it.child("contentTitle").value.toString()
                        val description = it.child("contentDescription").value.toString()

                        binding.contentTitleTextView.text =title
                        binding.contentDescriptionTextView.text = Html.fromHtml(description,0)

                        println("########################################################################### ${it}")
                        print("")

                        if(it.hasChild("quiz")){
                            binding.nextButton.visibility = View.VISIBLE
                            it.child("quiz").children.forEach {data ->
                                answer = data.child("answer").value.toString()
                                question = data.child("question").value.toString()
                                options = data.child("options").value.toString()
                                type = data.child("quizType").value.toString()
                                println("###########################################################################type type ${type}")


                            }
                        }else{
                            binding.nextButton.visibility = View.GONE
                        }
                    }
                }

            })

        binding.nextButton.setOnClickListener {
            if(type == "DragAndDrop"){
                val action = ContentFragmentDirections.actionContentFragmentToDragAndDropQuizFragment2(question, answer, options)
                findNavController().navigate(action)
            }
            if(type == "WordOrder"){
                val action = ContentFragmentDirections.actionContentFragmentToWordsQuizFragment(question, answer, options)
                findNavController().navigate(action)
            }
            if(type == "MOQ"){
                val action = ContentFragmentDirections.
                ContentFragmentToMultiOptionQuestion(args.sectionIndex,
                    args.planetId.toString())
                Navigation.findNavController(binding.root)
                    .navigate(action)
            }
        }

        return binding.root
    }

}