package com.example.madcamp_androidapp

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcamp_androidapp.databinding.ItemPhotoBinding

class SwipePhotoAdapter(private val PhotoList: List<Photo>) : RecyclerView.Adapter<SwipePhotoAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            Glide.with(binding.photoImageViewSwipe.context)
                .load(photo.imageURI)
                .fitCenter()
                .into(binding.photoImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_swipe, parent, false)
        return ViewHolder(ItemPhotoBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = PhotoList[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int {
        return PhotoList.size
    }
}