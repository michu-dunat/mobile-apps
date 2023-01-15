package com.example.birthdaytracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.birthdaytracker.R
import com.example.birthdaytracker.model.Person
import java.time.LocalDate

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
        val cake: ImageView = view.findViewById(R.id.cake)

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
        if (isTodayBirthdayOfGivenPerson(position)
        ) {
            holder.cake.visibility = View.VISIBLE
        } else {
            holder.cake.visibility = View.INVISIBLE
        }
    }

    private fun isTodayBirthdayOfGivenPerson(position: Int) =
        (dataSet[position].birthday!!.split("-")[1].toInt() == LocalDate.now().monthValue
                && dataSet[position].birthday!!.split("-")[2].toInt() == LocalDate.now().dayOfMonth)

    override fun getItemCount(): Int {
        return dataSet.size
    }
}