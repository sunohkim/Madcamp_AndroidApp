package com.example.madcamp_androidapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class BoardAdapter(private var itemList: ArrayList<BoardItem>) :
        RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

//    init {
//        this.itemList = itemList
//    }
    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        Log.w("plz", "onCreateViewHolder")
        //Log.w("plz", "plz")
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
//        holder.tv_name.text = itemList[position].name
//        holder.tv_num.text = itemList[position].num
//        Log.w("plz", "onBindViewHolder")
        holder.bind(itemList[position])

        holder.btn_delete.setOnClickListener {
            removeContact(position)
        }

        // Log.w("Check", holder.tv_name.text.toString())
    }

    override fun getItemCount(): Int {
//        Log.w("plz", "getItemCount")
//        return itemList.count()
        return itemList.size
    }

    // 검색 엔진 만들기 위한 함수
    fun setItems(list: ArrayList<BoardItem>) {
        Log.w("plz", "setItems")
        itemList = list
        notifyDataSetChanged()

    }

    private fun removeContact(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }


    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_name = itemView.findViewById<TextView>(R.id.tv_name)
        val tv_num = itemView.findViewById<TextView>(R.id.tv_num)
        val btn_delete = itemView.findViewById<Button>(R.id.btn_delete)

        fun bind(item: BoardItem) {
            tv_name.text = item.name
            tv_num.text = item.num
        }
    }
}