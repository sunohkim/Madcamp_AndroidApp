package com.example.madcamp_androidapp

import android.content.pm.PackageManager
import android.os.Build
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

    companion object {
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1
    }

    private lateinit var binding: FragmentGalleryBinding
    private val permissionList = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    // 권한 허용 알림창에서 허용/거부 버튼을 누른 직후 동작 관련 부분
    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        result.forEach {
            // 거부 버튼을 눌렀을 때
            if (!it.value) {
                Toast.makeText(context, "권한 동의 필요", Toast.LENGTH_SHORT).show()
            }
            // 허용 버튼을 눌렀을 때
            else {
                getAllImages()
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
    }

    // 권한 허용 여부 알림창이 나타나지 않은 경우에 대한 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                // 권한이 허용된 경우
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAllImages()
                }
                // 권한이 거부된 경우
                else {
                    Toast.makeText(context, "권한 동의 필요", Toast.LENGTH_SHORT).show()
                }
            }
        }
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

            cursor?.use {
                // 이미지의 id, 이름 정보는 이미지 생성과 관련 없으므로 주석 처리 함.
                // val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                // val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val pathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

                // 갤러리 내 각 이미지에 접근 후 이미지의 URI를 가져와 리스트에 저장
                while (it.moveToNext()) {
                    // 이미지의 id, 이름 정보는 이미지 생성과 관련 없으므로 주석 처리 함.
                    // val id = it.getLong(idColumn)
                    // val name = it.getString(nameColumn)
                    val path = it.getString(pathColumn)

                    imageList.add(Photo(path))
                }
            }

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
            // adapter 연결
            recyclerView1.adapter = PhotoAdapter(photoList1)
            recyclerView2.adapter = PhotoAdapter(photoList2)
            recyclerView3.adapter = PhotoAdapter(photoList3)

            val viewPager = binding.viewPager

            viewPager.adapter = SwipePhotoAdapter(imageList)

            val toggleButton = binding.toggleButton
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    recyclerView1.visibility = View.GONE
                    recyclerView2.visibility = View.GONE
                    recyclerView3.visibility = View.GONE
                    viewPager.visibility = View.VISIBLE
                } else {
                    recyclerView1.visibility = View.VISIBLE
                    recyclerView2.visibility = View.VISIBLE
                    recyclerView3.visibility = View.VISIBLE
                    viewPager.visibility = View.GONE
                }
            }

        }
    }
}