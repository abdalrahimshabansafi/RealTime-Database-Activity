package com.example.realtimeexample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecAdapter (private val context: Context, private var dataList: List<DataClass>) :
    RecyclerView.Adapter<RecAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.recName.text = dataList[position].name
        holder.recNumber.text = dataList[position].number
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recName: TextView
        var recNumber: TextView
        init {
            recName = itemView.findViewById(R.id.nameView)
            recNumber = itemView.findViewById(R.id.numberView)
        }
    }

}