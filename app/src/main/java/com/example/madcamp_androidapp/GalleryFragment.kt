package com.example.madcamp_androidapp

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_androidapp.GalleryFragment.Companion.READ_EXTERNAL_STORAGE_REQUEST_CODE
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

    private val imageList = mutableListOf<Photo>()
    private val photoList1 = mutableListOf<Photo>()
    private val photoList2 = mutableListOf<Photo>()
    private val photoList3 = mutableListOf<Photo>()
    private lateinit var adapter1: PhotoAdapter
    private lateinit var adapter2: PhotoAdapter
    private lateinit var adapter3: PhotoAdapter
    private var totalImages = 0

    // 권한 허용 알림창에서 허용/거부 버튼을 누른 직후 동작 관련 부분
    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        result.forEach {
            // 거부 버튼을 눌렀을 때
            if (!it.value) {
                Toast.makeText(context, "권한 동의 필요", Toast.LENGTH_SHORT).show()
                connectAdapter()
            }
            // 허용 버튼을 눌렀을 때
            else {
                getAllImages(
                    callback = { isget ->
                        if (isget) {
                            connectAdapter()
                        } else {
                            Toast.makeText(context, "이미지 로드에 실패했습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    onError = { error ->
                        Log.w("Error: ", "Failed to load images due to unexpected error.")
                    })
            }
        }
    }

    private val cameraLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            // 사진 찍기 성공
            Toast.makeText(context, "사진 찍기 성공", Toast.LENGTH_SHORT).show()
            Log.w("check3:", "check3")

            // 찍은 사진을 imageList에 추가
            val lastPhotoUri = getLastTakenPhotoUri()
            Log.w("check1:", "check1")
            if (lastPhotoUri != null) {
                Log.w("check2:", "check2")
                val newPhoto = Photo(lastPhotoUri.toString())
                imageList.add(newPhoto)

                addInPhotoList(newPhoto)
                Log.w("uri: ", newPhoto.imageURI)

                // RecyclerView 갱신
                connectAdapter()

            }
        } else {
            // 사진 찍기 실패
            Toast.makeText(context, "사진 찍기 실패", Toast.LENGTH_SHORT).show()
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

        val cameraButton = binding.cameraButton
        cameraButton.setOnClickListener {
            takePhoto()
        }

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
                    getAllImages(
                        callback = { isget ->
                            if (isget) {
                                connectAdapter()
                            } else {
                                Toast.makeText(context, "이미지 로드에 실패했습니다.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        onError = { error ->
                            Log.w("Error: ", "Failed to load images due to unexpected error.")
                        })
                }
                // 권한이 거부된 경우
                else {
                    Toast.makeText(context, "권한 동의 필요", Toast.LENGTH_SHORT).show()
                    connectAdapter()
                }
            }
        }
    }

    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("cameraLauncher call!", "ok")
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        } else {
            // 카메라 권한이 없는 경우
            Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLastTakenPhotoUri(): Uri? {
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor = requireActivity().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_TAKEN + " DESC"
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val imageId = it.getLong(idColumn)
                return Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageId.toString()
                )
            }
        }

        return null
    }

    private fun getAllImages(callback: (Boolean) -> Unit, onError: (Error) -> Unit) {
        var isget = false

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

            // 각 recyclerView에 들어갈 이미지들을 순서대로 배치
            for (i in imageList) {
                if (totalImages%3 == 0) {
                    photoList1.add(i)
                }
                else if (totalImages%3 == 1) {
                    photoList2.add(i)
                }
                else {
                    photoList3.add(i)
                }
                totalImages += 1
            }
            isget = true
            callback(isget)
        }
    }

    private fun connectAdapter() {
        // 3개의 recyclerView를 선언
        val recyclerView1 = binding.recyclerView1
        val layoutManager1 = LinearLayoutManager(requireContext())
        adapter1 = PhotoAdapter(photoList1)
        recyclerView1.layoutManager = layoutManager1
        recyclerView1.adapter = adapter1

        val recyclerView2 = binding.recyclerView2
        val layoutManager2 = LinearLayoutManager(requireContext())
        adapter2 = PhotoAdapter(photoList2)
        recyclerView2.layoutManager = layoutManager2
        recyclerView2.adapter = adapter2

        val recyclerView3 = binding.recyclerView3
        val layoutManager3 = LinearLayoutManager(requireContext())
        adapter3 = PhotoAdapter(photoList3)
        recyclerView3.layoutManager = layoutManager3
        recyclerView3.adapter = adapter3

        // viewPager 선언
        val viewPager = binding.viewPager

        // adapter 연결
        viewPager.adapter = SwipePhotoAdapter(imageList)

        // 초기 스와이프 화면은 보이지 않는 상태로 설정
        viewPager.visibility = View.GONE

        // 토글 버튼에 따라 보이는 화면 전환
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

    private fun addInPhotoList(photo: Photo) {
        if (totalImages%3 == 0) {
            photoList1.add(photo)
        }
        else if (totalImages%3 == 1) {
            photoList2.add(photo)
        }
        else {
            photoList3.add(photo)
        }
        totalImages += 1
        for (i in imageList) {
            Log.w("oldPhoto: ", i.imageURI)
        }
        Log.w("newPhoto: ", photo.imageURI)

        adapter1.notifyDataSetChanged()
        adapter2.notifyDataSetChanged()
        adapter3.notifyDataSetChanged()
    }
}