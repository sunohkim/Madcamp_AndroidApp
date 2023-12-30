package com.example.madcamp_androidapp

import android.app.Instrumentation.ActivityResult
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_androidapp.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private val photoList = mutableListOf<Photo>()
    private val permissionList = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        result.forEach {
            if (!it.value) {
                Toast.makeText(context, "권한 동의 필요", Toast.LENGTH_SHORT).show()
        }
    }
    private val readImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        photoList.add(Photo(uri.toString()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission.launch(permissionList)
        readImage.launch("image/*")

        val recyclerView1 = binding.recyclerView1
        val layoutManager1 = LinearLayoutManager(requireContext())
        val photoList1 = mutableListOf<Photo>()
        val recyclerView2 = binding.recyclerView2
        val layoutManager2 = LinearLayoutManager(requireContext())
        val photoList2 = mutableListOf<Photo>()
        val recyclerView3 = binding.recyclerView3
        val layoutManager3 = LinearLayoutManager(requireContext())
        val photoList3 = mutableListOf<Photo>()

        recyclerView1.layoutManager = layoutManager1
        recyclerView2.layoutManager = layoutManager2
        recyclerView3.layoutManager = layoutManager3

        val photoList = getPhotoList()
        for (i in photoList.indices) {
            if (i%3 == 0) {
                photoList1.add(photoList[i])
            }
            else if (i%3 == 1) {
                photoList2.add(photoList[i])
            }
            else {
                photoList3.add(photoList[i])
            }
        }

        recyclerView1.adapter = PhotoAdapter(photoList1)
        recyclerView2.adapter = PhotoAdapter(photoList2)
        recyclerView3.adapter = PhotoAdapter(photoList3)
    }

    private fun getPhotoList(): List<Photo> {
        val photoList = mutableListOf<Photo>()
        //todo: photoList에 사진 데이터 추가
        for (i in 1..20) {
            //photoList.add(Photo("https://ifh.cc/g/Cydxpa.jpg"))
            photoList.add(Photo("https://cdn.pixabay.com/photo/2021/08/03/07/03/orange-6518675_960_720.jpg"))
            photoList.add(Photo("https://ifh.cc/g/zBbY9g.jpg"))
        }
        Log.w("check", photoList[0].imageUri)
        return photoList
    }
}