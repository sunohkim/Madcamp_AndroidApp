package com.example.madcamp_androidapp

import android.content.ClipData.Item
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_androidapp.databinding.ItemPhotoBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

class PhotoAdapter(private val photoList: List<Photo>): RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            Log.w("check", "ch4")
            //binding.photoImageView = photo
            Glide.with(binding.root.context)
                .load(photo.imageUri)
                .fitCenter()
                //.into(binding.photoImageView)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //val imageRatio = resource?.intrinsicWidth?.toFloat() ?: 1f / (resource?.intrinsicHeight?.toFloat() ?: 1f)
                        val imageRatio = resource?.intrinsicWidth.toString().toFloat() / resource?.intrinsicHeight.toString().toFloat()
                        Log.w(photo.imageUri, imageRatio.toString())

                        val layoutParams = binding.photoImageView.layoutParams
                        layoutParams.width = binding.photoImageView.width
                        layoutParams.height = (layoutParams.width / imageRatio).toInt()
                        Log.w("----------", "width and height")
                        Log.w(layoutParams.width.toString(), layoutParams.height.toString())
                        binding.photoImageView.layoutParams = layoutParams

                        return false
                    }
                })
                .into(binding.photoImageView)

            //binding.executePendingBindings()
        }
    }

    // ViewHolder를 생성 후 리턴
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.w("check", "ch2")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(ItemPhotoBinding.bind(view))
    }

    // View에 내용이 씌워질 때 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.w("check", "ch3")
        val photo = photoList[position]
        Log.w("uri: ", photo.imageUri)
        holder.bind(photo)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}