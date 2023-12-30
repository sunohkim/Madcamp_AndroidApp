package com.example.madcamp_androidapp

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_androidapp.databinding.ItemPhotoBinding
import com.bumptech.glide.Glide

class PhotoAdapter(private val photoList: List<Photo>): RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            Log.w("glide: ", photo.imageUri)
            Glide.with(binding.root.context)
                .load(photo.imageUri)
                .fitCenter()
                .into(binding.photoImageView)
        }
    }

    // ViewHolder를 생성 후 리턴
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(ItemPhotoBinding.bind(view))
    }

    // View에 내용이 씌워질 때 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photoList[position]
        holder.bind(photo)

        // 이미지를 클릭했을 때의 동작
        holder.itemView.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val view = inflater.inflate(R.layout.dialog_image, null)
            val imageView = view.findViewById<ImageView>(R.id.dialogImageView)

            Glide.with(holder.itemView.context)
                .load(photo.imageUri)
                .into(imageView)

            // '닫기' 버튼을 추가했더니 예쁘지 않아서 주석처리함
            builder.setView(view)
                .setPositiveButton("닫기") { dialog, _ ->
                    dialog.dismiss()
                }

            val alertDialog = builder.create()
            alertDialog.show()

        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}