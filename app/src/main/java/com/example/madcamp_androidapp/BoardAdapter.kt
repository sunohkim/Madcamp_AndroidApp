package com.example.madcamp_androidapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BoardAdapter(private var itemList: ArrayList<BoardItem>) :
        RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

//    init {
//        this.itemList = itemList
//    }
    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.tv_name.text = itemList[position].name
        holder.tv_num.text = itemList[position].num

        // Log.w("Check", holder.tv_name.text.toString())
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    // 검색 엔진 만들기 위한 함수
    fun setItems(list: ArrayList<BoardItem>) {
        itemList = list
        notifyDataSetChanged()

    }


    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_name = itemView.findViewById<TextView>(R.id.tv_name)
        val tv_num = itemView.findViewById<TextView>(R.id.tv_num)
    }
        }