package com.example.madcamp_androidapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_androidapp.databinding.FragmentGalleryBinding
import com.example.madcamp_androidapp.ImageDecoration
import java.io.File

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        // 가로 이미지 개수 설정
        val spanCount = 3
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        recyclerView.layoutManager = layoutManager
        // 리사이클러뷰 아이템 간 간격 설정
        //recyclerView.addItemDecoration(ImageDecoration(100, spanCount))
        recyclerView.adapter = PhotoAdapter(getPhotoList())
    }

    private fun getPhotoList(): List<Photo> {
        val photoList = mutableListOf<Photo>()
        //todo: photoList에 사진 데이터 추가
        for (i in 1..40) {
            photoList.add(Photo("https://cdn.pixabay.com/photo/2021/08/03/07/03/orange-6518675_960_720.jpg"))
            photoList.add(Photo("https://ifh.cc/g/zBbY9g.jpg"))
        }
        Log.w("check", photoList[0].imageUri)
        return photoList
    }
}