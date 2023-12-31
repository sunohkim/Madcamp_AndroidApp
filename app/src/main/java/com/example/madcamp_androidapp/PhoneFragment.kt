package com.example.madcamp_androidapp

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
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
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_androidapp.databinding.ActivityMainBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [PhoneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhoneFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var view: View
    private lateinit var addNum: Button

    // 검색 기능 만들기
    // 검색시 같은 이름이 있는 아이템이 담길 리스트
    private val searchList = ArrayList<BoardItem>()
    // recyclerView에 추가할 아이템 리스트 (원래 리스트), 인터넷 코드에선 original_list
    private val itemList = ArrayList<BoardItem>()
    // 어댑터 흠... 일단 인터넷을 믿어보자
    private lateinit var adapter: BoardAdapter
    private lateinit var editText: EditText

    // permission 부분
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_phone, container, false)

//        //permission 부분
//        // 사용자가 퍼미션 허용했는지 확인
//        binding = ActivityMainBinding.inflate(inflater, container, false)
//        val status = ContextCompat.checkSelfPermission(requireContext(), "android.permission.READ_CONTACTS")
//        if (status == PackageManager.PERMISSION_GRANTED) {
//            Log.d("test", "permission granted")
//        } else {
//            // 퍼미션 요청 다이얼로그 표시
//            requestPermissions(arrayOf<String>("android.permission.READ_CONTACTS"), 100)
//            Log.d("test", "permission denied")
//        }




//        // 휴대폰 주소록 불러오기
//        val cr: ContentResolver? = context?.contentResolver // fragment에서는.. context.도 붙이고 ?도 붙여야하는 경우가 많은가봄
//        val cur: Cursor? = cr?.query(Contacts.CONTENT_URI, null, null, null, null)
//
//        if (cur?.count ?: 0 > 0) {
//            var line: String
//            while (cur?.moveToNext() == true) {
//                val id: Int = cur.getInt(cur.getColumnIndex(Contacts._ID))
//                line = String.format("%4d", id)
//                val name: String = cur.getString(cur.getColumnIndex(Contacts.DISPLAY_NAME))
//                line += " $name"
//
//                if ("1" == cur.getString(cur.getColumnIndex(Contacts.HAS_PHONE_NUMBER))) {
//                    val pCur: Cursor? = cr?.query(
//                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        null,
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
//                        arrayOf(id.toString()),
//                        null
//                    )
//
//                    var i = 0
//                    val pCount = pCur?.count?: 0
//                    val phoneNum = arrayOfNulls<String>(pCount)
//                    val phoneType = arrayOfNulls<String>(pCount)
//
//                    while (pCur?.moveToNext() == true) {
//                        phoneNum[i] = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                        line += " ${phoneNum[i]}"
//                        itemList.add(BoardItem(name, phoneNum[i]!!))
//                        Log.w("numplz", phoneNum[i]!!)
//                        phoneType[i] = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
//                        i++
//                    }
//                    pCur?.close()
//                }
//                //numbook.add(line)
//                Log.w("letscheck", line)
//                Log.w("nameplz", name)
//                line = ""
//            }
//            cur?.close()
//        }






        // 새 연락처 추가하는 Activity로 넘어가는 코드
        addNum = view.findViewById(R.id.btnAdd)
        addNum.setOnClickListener {
            val intent = Intent(activity, PhoneAddNumber::class.java)
            // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }




        // 이 rv_board가 원래 코드의 recyclerView임. 어댑터가 연결 된 것!
        val rv_board = view.findViewById<RecyclerView>(R.id.rv_board)
        //rv_board.layoutManager = LinearLayoutManager(context)
        //val itemList = ArrayList<BoardItem>()

        itemList.add(BoardItem("Hany Song", "010-4572-1946"))
        itemList.add(BoardItem("Hani Son", "010-1946-4572"))
        itemList.add(BoardItem("김가현", "010-0000-0000"))
        itemList.add(BoardItem("임나현", "010-1111-1111"))
//        itemList.add(BoardItem("박다현", "010-2222-2222"))
//        itemList.add(BoardItem("송라현", "010-3333-3333"))
//        itemList.add(BoardItem("최마현", "010-4444-4444"))
//        itemList.add(BoardItem("이바현", "010-5555-5555"))
//        itemList.add(BoardItem("옹사현", "010-6666-6666"))
//        itemList.add(BoardItem("차아현", "010-7777-7777"))
//        itemList.add(BoardItem("강자현", "010-8888-8888"))
//        itemList.add(BoardItem("현차현", "010-9999-9999"))
//        itemList.add(BoardItem("장카현", "010-1234-5678"))
//        itemList.add(BoardItem("하타현", "010-9876-5432"))
//        itemList.add(BoardItem("마파현", "010-1213-1415"))
//        itemList.add(BoardItem("정하현", "010-1617-1819"))
//        itemList.add(BoardItem("김가현", "010-0000-0000"))
//        itemList.add(BoardItem("임나현", "010-1111-1111"))
//        itemList.add(BoardItem("박다현", "010-2222-2222"))
//        itemList.add(BoardItem("송라현", "010-3333-3333"))
//        itemList.add(BoardItem("최마현", "010-4444-4444"))
//        itemList.add(BoardItem("이바현", "010-5555-5555"))
//        itemList.add(BoardItem("옹사현", "010-6666-6666"))
//        itemList.add(BoardItem("차아현", "010-7777-7777"))
//        itemList.add(BoardItem("강자현", "010-8888-8888"))
//        itemList.add(BoardItem("현차현", "010-9999-9999"))
//        itemList.add(BoardItem("장카현", "010-1234-5678"))
//        itemList.add(BoardItem("하타현", "010-9876-5432"))
//        itemList.add(BoardItem("마파현", "010-1213-1415"))
//        itemList.add(BoardItem("정하현", "010-1617-1819"))
        // 여기서 문제. 장카현 아래로는 스크롤이 안된다. 이건 constraint의 문제인가?
        // 네비게이션 바로 가려져서 그런건가? 음 그것도 아닌 것 같기도 하고... 나중에 해결하자






        // PhoneAddNumber에서 가져온 데이터 추가
        //val getintent = Intent(activity, NextActivity)
        //val NewName = WannaIntent.getStringExtra("name")

//        val extra: Bundle? = arguments
//        if (extra != null) {
//            val newname = extra.getString("name")
//            val newnum = extra.getString("num")
//            Log.w("Check", newname.toString())
//            Log.w("Check", newnum.toString())
//            itemList.add(BoardItem(newname!!, newnum!!))
//        }

//        val bundle: Bundle? = arguments
//        val newname = bundle?.getString("name")
//        val newnum = bundle?.getString("num")
//        Log.w("reallyCheck", newname.toString())
//        Log.w("reallyCheck", newnum.toString())
//        itemList.add(BoardItem(newname.toString(), newnum.toString()))
//        for(i: Int in 1..itemList.size)
//            Log.w("itemList", itemList[i].name)


        // 한 번 시도
//        val newphonenumadapter = CustomAdapter(requireContext())
//        rv_board.adapter = newphonenumadapter





        // 검색 엔진
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

        // rv_board가 recyclerview임. 위에 주석 적어놨음
        rv_board.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        //val boardAdapter = BoardAdapter(itemList)
        //rv_board.adapter = boardAdapter
        adapter = BoardAdapter(itemList)
        rv_board.adapter = adapter





        
        return view
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Log.d("test", "permission granted")
//        } else {
//            Log.d("test", "permission denied")
//        }
//    }

//    private inner class CustomAdapter(private val context: Context) : BaseAdapter() {
//        override fun getCount(): Int {
//            return itemList.size
//        }
//
//        override fun getItem(position: Int): Any? {
//            return null
//        }
//
//        override fun getItemId(position: Int): Long {
//            return 0
//        }
//
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//            val itemView = View.inflate(context, R.layout.item_recycler_view, null)
//            val nameItem: TextView = itemView.findViewById(R.id.txt_name)
//            val numItem: TextView = itemView.findViewById(R.id.txt_number)
//
//            itemList[position] = BoardItem(nameItem.text.toString(), numItem.text.toString())
//            return itemView
//        }
//    }

//    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
//        var ft: FragmentTransaction = fragmentManager.beginTransaction()
//        ft.detach(fragment).attach(fragment).commit()
//    }

    companion object {
        lateinit var arguments: Bundle
    }

}