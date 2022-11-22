package com.example.a6.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a6.MyDBHandler
import com.example.a6.R

class ItemAdapter(
    private val context: Context,
    private val dataSet: ArrayList<String>,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_row_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val dbHandler = MyDBHandler(context, null, null, 1)
        val items = dbHandler.findAllMajors()
        holder.textView.text = items[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(items[position])
        }
    }

    override fun getItemCount(): Int {
        val dbHandler = MyDBHandler(context, null, null, 1)
        return dbHandler.findAllMajors().size
    }

    class OnClickListener(val clickListener: (major: String) -> Unit) {
        fun onClick(major: String) = clickListener(major)
    }
}