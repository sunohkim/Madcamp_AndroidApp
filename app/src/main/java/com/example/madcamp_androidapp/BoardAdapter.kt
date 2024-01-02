package com.example.madcamp_androidapp

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class BoardAdapter(private var itemList: ArrayList<BoardItem>) :
        RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {
    
    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(itemList[position])
        
        // 삭제 버튼 (btn_delete) 눌렀을 때, 항목 삭제
        holder.btn_delete.setOnClickListener {
            val builder = AlertDialog.Builder(holder.btn_delete.context)
            val inflater = LayoutInflater.from(holder.btn_delete.context)
            val view = inflater.inflate(R.layout.dialog_image, null)

            // '삭제' 아이콘을 눌렀을 때 나오는 확인 메시지
            builder.setMessage("연락처를 삭제하시겠습니까?")
                .setPositiveButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton("삭제") { dialog, _ ->
                    removeContact(position)
                    dialog.dismiss()
                }
            val alertDialog = builder.create()
            alertDialog.show()

            // '삭제' 버튼 색상 지정
            val negativeButton: Button = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            negativeButton.setTextColor(ContextCompat.getColor(holder.btn_delete.context, R.color.red)) // 빨간색으로 변경
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // 검색 엔진 만들기 위한 함수
    fun setItems(list: ArrayList<BoardItem>) {
        itemList = list
        notifyDataSetChanged()

    }

    // recyclerView 중 하나의 항목 삭제하는 함수
    private fun removeContact(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }


    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Adapter에서 text나 button을 쓰려면, 여기서 선언해줘야 함
        val tv_name = itemView.findViewById<TextView>(R.id.tv_name)
        val tv_num = itemView.findViewById<TextView>(R.id.tv_num)
        val btn_delete = itemView.findViewById<Button>(R.id.btn_delete)

        // item의 name과 num을 textView에 적힌 대로 할당해주는 함수
        fun bind(item: BoardItem) {
            tv_name.text = item.name
            tv_num.text = item.num
        }
    }
}