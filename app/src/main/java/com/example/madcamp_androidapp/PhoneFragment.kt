package com.example.madcamp_androidapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_androidapp.databinding.FragmentGalleryBinding
import com.example.madcamp_androidapp.databinding.FragmentPhoneBinding

class PhoneFragment : Fragment() {

    private lateinit var binding: FragmentPhoneBinding
    private lateinit var view: View // PhoneFragment의 View
    private lateinit var addNum: Button // PhoneAddNumber activity로 넘어가는, 번호 추가 버튼
    private lateinit var adapter: BoardAdapter // 어댑터
    private lateinit var editText: EditText // 검색 용도의 EditText
    private lateinit var rv_board: RecyclerView // view의 RecyclerView

    private val searchList = ArrayList<BoardItem>() // 검색시 같은 이름이 있는 아이템이 담길 리스트
    private var itemList = ArrayList<BoardItem>() // recyclerView에 보여지는 원래 기본 리스트
    private val permissionList = arrayOf(android.Manifest.permission.READ_CONTACTS)

    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        result.forEach {
            // 거부 버튼을 눌렀을 때
            if (!it.value) {
                Toast.makeText(context, "권한 동의 필요", Toast.LENGTH_SHORT).show()
            }
            // 허용 버튼을 눌렀을 때
            else {
                getAllContacts()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission.launch(permissionList)
    }

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneBinding.inflate(inflater, container, false)

        return binding.root
    }

    // PhoneAddNumber에서 가지고 온 Result(name, num)을 itemList에 추가하는 과정
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHONE_ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val newname = it.getStringExtra("name")
                val newnum = it.getStringExtra("num")

                var parsingNum = ""
                var numlength: Int = 0
                if (newnum != null) {
                    numlength = newnum.length
                } // 전화번호 길이

                if (newnum != null) {
                    if (newnum.substring(0, 1) == "0") {
                        // 0으로 시작하는 경우는 대체로 -가 들어감
                        if (numlength >= 9 && numlength <= 10) {
                            if (newnum.substring(1, 2) == "2") {
                                // 02로 시작하는 번호인 경우
                                // 02-000-0000, 02-0000-0000 모두 가능
                                val mid = newnum.substring(2 until numlength-4)
                                val last = newnum.substring(numlength-4)
                                parsingNum = "02-" + mid + "-" + last
                            } else if (numlength == 10) {
                                // 그 외 지역번호, 혹은 옛날 전화번호
                                // 051-000-0000, 017-000-0000 모두 가능
                                // 얘네는 무조건 10자리
                                val first = newnum.substring(0 until 3)
                                val mid = newnum.substring(3 until numlength-4)
                                val last = newnum.substring(numlength-4)
                                parsingNum = first + "-" + mid + "-" + last
                            } else {
                                // 02로 시작하지도 않고 10자리도 아니면
                                // (즉 02로 시작하지 않는 9자리면) 그냥 그대로
                            }
                        } else if (numlength == 11) {
                            // 일반 전화번호, 또는 02를 제외한 지역번호
                            // 010-0000-0000, 032-0000-0000
                            val first = newnum.substring(0 until 3)
                            val mid = newnum.substring(3 until numlength-4)
                            val last = newnum.substring(numlength-4)
                            parsingNum = first + "-" + mid + "-" + last
                        } else {
                            // 9, 10, 11자리보다 더 긴 경우 그냥 붙여서 넣기
                            parsingNum = newnum
                        }
                    } else {
                        // 0이외의 숫자로 시작하면 그냥 붙여서 넣기
                        parsingNum = newnum
                    }
                }
                newname?.let { name ->
                    parsingNum?.let { num ->
                        // 새로운 연락처 정보를 추가하고 이름 순으로 정렬
                        itemList.add(BoardItem(name, num))
                        itemList.sortBy { it.name }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_CONTACTS_REQUEST_CODE -> {
                // 권한이 허용된 경우
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAllContacts()
                }
                // 권한이 거부된 경우
                else {
                    Toast.makeText(context, "권한 동의 필요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getAllContacts() {
        // view, adapter, rv_board lateinit 정의 및 RecyclerView와 adapter 연결
        adapter = BoardAdapter(itemList)
        rv_board = binding.rvBoard
        rv_board.layoutManager = LinearLayoutManager(requireContext())
        rv_board.adapter = adapter

        // 휴대폰 주소록 불러오기
        val contentResolver: ContentResolver = requireContext().contentResolver

        // 연락처 데이터의 커서 가져오기
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        // 커서가 비어 있지 않은 경우 데이터 읽기
        cursor?.use {
            val nameColumnIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberColumnIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = if (nameColumnIndex != -1) it.getString(nameColumnIndex) else ""
                val phoneNumber = if (numberColumnIndex != -1) it.getString(numberColumnIndex) else ""

                // 리스트에 추가
                itemList.add(BoardItem(name, phoneNumber))
            }
            // 리스트를 이름 순으로 정렬
            itemList.sortBy { it.name }
        }
        cursor?.close()

        // 검색 엔진 만들기
        editText = binding.editSearch

        // editText 리스터 작성
        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val searchText = editText.text.toString()
                searchList.clear()

                if(searchText.isEmpty()) {
                    adapter.setItems(itemList)
                } else {
                    for (a in itemList.indices) {
                        if (itemList[a].name.toLowerCase().contains(searchText.toLowerCase())) {
                            searchList.add(itemList[a])
                        } else if (itemList[a].num.contains(searchText)) {
                            searchList.add(itemList[a])
                        }
                    }
                    adapter.setItems(searchList)
                }
            }
        })

        // 새 연락처 추가하는 Activity로 넘어가는 코드
        // addNum 버튼 눌러서 넘어감
        // activity 갔다가 돌아올 때는 무조건 Result를 가지고 돌아와야 함
        addNum = binding.btnAdd
        addNum.setOnClickListener {
            val intent = Intent(activity, PhoneAddNumber::class.java)
            startActivityForResult(intent, PHONE_ADD_REQUEST_CODE)
        }
    }

    companion object {
        private const val PHONE_ADD_REQUEST_CODE = 123
        private const val READ_CONTACTS_REQUEST_CODE = 1
    }

}