package com.thanaa.tarmeezapp

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thanaa.tarmeezapp.databinding.FragmentContentBinding


class ContentFragment : Fragment() {

    val args by navArgs<ContentFragmentArgs>()

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
                        binding.contentDescriptionTextView.text = Html.fromHtml(description)

                        println("########################################################################### ${it}")
                        print("")
                    }
                }

            })


        return binding.root
    }

}