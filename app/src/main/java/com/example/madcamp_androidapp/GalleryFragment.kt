package com.example.madcamp_androidapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_androidapp.databinding.FragmentGalleryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private val permissionList = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        result.forEach {
            if (!it.value) {
                Toast.makeText(context, "권한 동의 필요", Toast.LENGTH_SHORT).show()
            }
        }
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

        when {
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                getAllImages()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {

            }
            else -> {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
            }
        }

        //getAllImages()

    }


    private fun getAllImages() {
        val imageList = mutableListOf<Photo>()
        // 안드로이드 기기 내 갤러리에 접근
        GlobalScope.launch(Dispatchers.Main) {
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
            )

            val cursor = requireActivity().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
            )
            Log.w("check1", "ch1")

            cursor?.use {
                // 이미지의 id, 이름 정보는 이미지 생성과 관련 없으므로 주석 처리 함.
                // val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                // val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val pathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

                while (it.moveToNext()) {
                    // 이미지의 id, 이름 정보는 이미지 생성과 관련 없으므로 주석 처리 함.
                    // val id = it.getLong(idColumn)
                    // val name = it.getString(nameColumn)
                    val path = it.getString(pathColumn)

                    imageList.add(Photo(path))
                }
            }
            Log.w("check2", "ch2")
            // 3개의 recyclerView를 선언
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

            // 각 recyclerView에 들어갈 이미지들을 순서대로 배치
            for (i in imageList.indices) {
                if (i%3 == 0) {
                    photoList1.add(imageList[i])
                }
                else if (i%3 == 1) {
                    photoList2.add(imageList[i])
                }
                else {
                    photoList3.add(imageList[i])
                }
            }
            recyclerView1.adapter = PhotoAdapter(photoList1)
            recyclerView2.adapter = PhotoAdapter(photoList2)
            recyclerView3.adapter = PhotoAdapter(photoList3)
        }
    }
}