package com.thanaa.tarmeezapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thanaa.tarmeezapp.data.Planet
import com.thanaa.tarmeezapp.data.Section
import com.thanaa.tarmeezapp.databinding.FragmentHomeBinding
import com.thanaa.tarmeezapp.databinding.FragmentSectionsBinding
import com.thanaa.tarmeezapp.databinding.ItemRowBinding

class SectionsFragment : Fragment() {
    private var _binding: FragmentSectionsBinding? = null
    private val binding get() = _binding!!

    private var sections = ArrayList<Section>()
    val sectionsList: MutableList<Section> = mutableListOf()
    var adapter = SectionsAdapter(sections)

    private val args by navArgs<SectionsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSectionsBinding.inflate(inflater, container, false)
        sectionsList.clear()

        FirebaseDatabase.getInstance().reference
            .child("Planet")
            .child(args.planetId.toString())
            .child("section")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                        val title = it.child("sectionTitle").value.toString()
                        val section = Section(title,"")
                        sectionsList.add(section)
                        println("########################################################################### ${it.child("sectionTitle").value}")

                    }
                    adapter.setData(sectionsList)
                }

            })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class SectionsAdapter(private var sectionsList: List<Section>) :
        RecyclerView.Adapter<SectionsAdapter.ViewHolder>() {

        inner class ViewHolder(val itemRowBinding: ItemRowBinding) : RecyclerView.ViewHolder(itemRowBinding.root) {

            fun bind(section: Section) {
                itemRowBinding.sectionTitle.text = section.sectionTitle
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding : ItemRowBinding = ItemRowBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding)
        }

        override fun getItemCount(): Int = sectionsList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val sectionItem = sectionsList[position]
            holder.bind(sectionItem)

            holder.itemView.setOnClickListener {
                val action = SectionsFragmentDirections.actionSectionsFragmentToContentFragment(position,sectionItem.sectionTitle,args.planetId)
                findNavController().navigate(action)
            }

        }

        fun setData(sections: List<Section>){
            this.sectionsList = sections
            notifyDataSetChanged()
        }

    }


}