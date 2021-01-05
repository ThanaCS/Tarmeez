package com.thanaa.tarmeezapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thanaa.tarmeezapp.R
import com.thanaa.tarmeezapp.data.Section

class SectionsAdapter(private val Section: List<Section>) :
    RecyclerView.Adapter<SectionsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(Section: Section, holder: ViewHolder) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        )
    }

    override fun getItemCount(): Int = Section.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookmarkItem = Section[position]
        holder.bind(bookmarkItem, holder)

    }


}