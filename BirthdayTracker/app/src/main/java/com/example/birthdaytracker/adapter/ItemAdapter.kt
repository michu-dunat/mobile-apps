package com.example.birthdaytracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.birthdaytracker.R
import com.example.birthdaytracker.model.Person

class ItemAdapter(
    private val context: Context,
    private val dataSet: List<Person>,
    private val OnItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(view) {
        val displayName: TextView = view.findViewById(R.id.displayName)
        val birthdayDate: TextView = view.findViewById(R.id.birthdayDate)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(adapterLayout, OnItemClickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.displayName.text =
            dataSet[position].firstName.plus(" ").plus(dataSet[position].lastName)
        holder.birthdayDate.text = dataSet[position].birthday
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}