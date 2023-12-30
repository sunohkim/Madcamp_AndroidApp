package com.example.madcamp_androidapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_androidapp.databinding.FragmentGalleryBinding

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

        val recyclerView1 = binding.recyclerView1
        val layoutManager1 = LinearLayoutManager(requireContext())
        val recyclerView2 = binding.recyclerView2
        val layoutManager2 = LinearLayoutManager(requireContext())
        val recyclerView3 = binding.recyclerView3
        val layoutManager3 = LinearLayoutManager(requireContext())

        recyclerView1.layoutManager = layoutManager1
        recyclerView2.layoutManager = layoutManager2
        recyclerView3.layoutManager = layoutManager3

        recyclerView1.adapter = PhotoAdapter(getPhotoList1())
        recyclerView2.adapter = PhotoAdapter(getPhotoList2())
        recyclerView3.adapter = PhotoAdapter(getPhotoList1())
    }

    private fun getPhotoList1(): List<Photo> {
        val photoList = mutableListOf<Photo>()
        //todo: photoList에 사진 데이터 추가
        for (i in 1..20) {
            photoList.add(Photo("https://ifh.cc/g/Cydxpa.jpg"))
            photoList.add(Photo("https://cdn.pixabay.com/photo/2021/08/03/07/03/orange-6518675_960_720.jpg"))
            photoList.add(Photo("https://ifh.cc/g/zBbY9g.jpg"))
        }
        Log.w("check", photoList[0].imageUri)
        return photoList
    }
    private fun getPhotoList2(): List<Photo> {
        val photoList = mutableListOf<Photo>()
        //todo: photoList에 사진 데이터 추가
        for (i in 1..20) {
            photoList.add(Photo(("https://ifh.cc/g/aLOq4x.jpg")))
            photoList.add(Photo("https://ifh.cc/g/zBbY9g.jpg"))
            photoList.add(Photo("https://cdn.pixabay.com/photo/2021/08/03/07/03/orange-6518675_960_720.jpg"))
        }
        Log.w("check", photoList[0].imageUri)
        return photoList
    }
}