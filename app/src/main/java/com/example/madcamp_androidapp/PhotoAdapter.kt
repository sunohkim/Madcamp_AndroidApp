package com.example.madcamp_androidapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_androidapp.databinding.ItemPhotoBinding
import com.bumptech.glide.Glide

class PhotoAdapter(private val photoList: List<Photo>): RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
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
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}