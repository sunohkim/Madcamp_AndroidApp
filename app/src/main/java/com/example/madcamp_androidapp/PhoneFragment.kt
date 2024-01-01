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

class PhoneFragment : Fragment() {

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

        // view, adapter, rv_board lateinit 정의 및 RecyclerView와 adapter 연결
        view = inflater.inflate(R.layout.fragment_phone, container, false)
        adapter = BoardAdapter(itemList)
        rv_board = view.findViewById<RecyclerView>(R.id.rv_board)
        rv_board.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_board.adapter = adapter




        // 휴대폰 주소록 불러오기
        val cr: ContentResolver? = context?.contentResolver // fragment에서는.. context.도 붙이고 ?도 붙여야하는 경우가 많은가 봄
        val cur: Cursor? = cr?.query(Contacts.CONTENT_URI, null, null, null, null)

        if (cur?.count ?: 0 > 0) {
            while (cur?.moveToNext() == true) {
                val id: Int = cur.getInt(cur.getColumnIndex(Contacts._ID))
                val name: String = cur.getString(cur.getColumnIndex(Contacts.DISPLAY_NAME))

                if ("1" == cur.getString(cur.getColumnIndex(Contacts.HAS_PHONE_NUMBER))) {
                    val pCur: Cursor? = cr?.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        arrayOf(id.toString()),
                        null
                    )

                    var i = 0
                    val pCount = pCur?.count?: 0
                    val phoneNum = arrayOfNulls<String>(pCount)
                    val phoneType = arrayOfNulls<String>(pCount)

                    while (pCur?.moveToNext() == true) {
                        phoneNum[i] = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        itemList.add(BoardItem(name, phoneNum[i]!!))
                        phoneType[i] = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                        i++
                    }
                    pCur?.close()
                }
            }
            cur?.close()
        }




        // dummy
        itemList.add(BoardItem("Hany Song", "010-4572-1946"))
        itemList.add(BoardItem("Hani Son", "010-1946-4572"))
        itemList.add(BoardItem("김가현", "010-0000-0000"))
        itemList.add(BoardItem("임나현", "010-1111-1111"))
        itemList.add(BoardItem("박다현", "010-2222-2222"))
        itemList.add(BoardItem("송라현", "010-3333-3333"))






        // 검색 엔진 만들기
        editText = view.findViewById(R.id.edit_search)
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
        // activity 갔다가 돌아올 때는 무조건 Result를 가지고 돌아와야 하
        addNum = view.findViewById(R.id.btnAdd)
        addNum.setOnClickListener {
            val intent = Intent(activity, PhoneAddNumber::class.java)
            startActivityForResult(intent, PHONE_ADD_REQUEST_CODE)
        }




        return view
    }

    // PhoneAddNumber에서 가지고 온 Result(name, num)을 itemList에 추가하는 과정
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHONE_ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val newname = it.getStringExtra("name")
                val newnum = it.getStringExtra("num")
                newname?.let { name ->
                    newnum?.let { num ->
                        itemList.add(BoardItem(name, num))
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

                }
                // 권한이 거부된 경우
                else {
                    Toast.makeText(context, "권한 동의 필요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val PHONE_ADD_REQUEST_CODE = 123
        private const val READ_CONTACTS_REQUEST_CODE = 1
    }

}