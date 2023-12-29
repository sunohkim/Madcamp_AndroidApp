package com.example.madcamp_androidapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BoardAdapter(val itemList: ArrayList<BoardItem>) :
        RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.tv_name.text = itemList[position].name
        holder.tv_num.text = itemList[position].num

        Log.w("Check", holder.tv_name.text.toString())
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_name = itemView.findViewById<TextView>(R.id.tv_name)
        val tv_num = itemView.findViewById<TextView>(R.id.tv_num)
    }
        }