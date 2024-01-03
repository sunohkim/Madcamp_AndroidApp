package com.example.madcamp_androidapp

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_androidapp.GalleryFragment.Companion.READ_EXTERNAL_STORAGE_REQUEST_CODE
import com.example.madcamp_androidapp.databinding.FragmentGalleryBinding
import com.github.angads25.toggle.interfaces.OnToggledListener
import com.github.angads25.toggle.model.ToggleableView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.Date
import java.text.SimpleDateFormat
import java.io.IOException

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
    private var currentPhotoUri: Uri? = null

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

    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            takePhoto()
        } else {
            Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // 사진 찍기 성공
            Toast.makeText(context, "사진이 갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show()

            // Uri 가져오기
            currentPhotoUri?.let { uri ->
                val bitmap = getBitmapFromUri(uri)

                if (bitmap != null) {
                    if(!imageExternalSave(requireContext(), bitmap, requireContext().getString(R.string.app_name))) {
                        Toast.makeText(context, "이미지 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                    // 비트맵을 가지고 갤러리에 저장
                }
                val newPhoto = Photo(uri.toString())
                imageList.add(newPhoto)
                addInPhotoList(newPhoto)
                connectAdapter()
            }
        } else {
            // 사진 찍기 실패
            Toast.makeText(context, "사진 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
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
        // 카메라 버튼을 눌렀을 때의 동작
        cameraButton.setOnClickListener {
            checkCameraPermissionAndTakePhoto()
        }

        checkPermission.launch(permissionList)
    }

    private fun checkCameraPermissionAndTakePhoto() {
        // 카메라 권한이 있는지 확인
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // 이미 권한이 있는 경우
                takePhoto()
            }
            else -> {
                // 권한이 없는 경우 권한 요청
                requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
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

    // 사진을 찍기 위해 기본 설정을 하는 함수
    private fun takePhoto() {
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // 새로운 파일을 만들어 Uri 저장
        val photoFile: File? = createImageFile()
        photoFile?.also {
            val photoUri: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.madcamp_androidapp.fileprovider",
                it
            )
            currentPhotoUri = photoUri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            // 카메라 실행
            cameraLauncher.launch(intent)
        }
    }

    // 파일을 만들어 Uri 반환
    private fun createImageFile(): File? {
        // 파일 이름 생성
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            // Uri를 사용하여 ContentResolver를 통해 InputStream을 열고, BitmapFactory를 사용하여 Bitmap으로 변환
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun imageExternalSave(context: Context, bitmap: Bitmap, path: String): Boolean {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {

            val rootPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString()
            val dirName = "/" + path
            val fileName = System.currentTimeMillis().toString() + ".png"
            val savePath = File(rootPath + dirName)
            savePath.mkdirs()

            val file = File(savePath, fileName)
            if (file.exists()) file.delete()

            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()

                //갤러리 갱신
                context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())
                    )
                )

                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    /*fun checkPermission(activity: Activty, permission: String): Boolean {
        val permissionChecker =
            ContextCompat.checkSelfPermission(activity.applicationContext, permission)

        return true
    }*/
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
        toggleButton.setOnToggledListener(object : OnToggledListener {
            override fun onSwitched(toggleableView: ToggleableView, isOn: Boolean) {
                if (isOn) {
                    recyclerView1.visibility = View.VISIBLE
                    recyclerView2.visibility = View.VISIBLE
                    recyclerView3.visibility = View.VISIBLE
                    viewPager.visibility = View.GONE
                } else {
                    recyclerView1.visibility = View.GONE
                    recyclerView2.visibility = View.GONE
                    recyclerView3.visibility = View.GONE
                    viewPager.visibility = View.VISIBLE
                }
            }
        })
        /*
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
        }*/
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