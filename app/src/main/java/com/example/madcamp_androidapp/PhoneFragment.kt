package com.example.madcamp_androidapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_phone, container, false)
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
        itemList.add(BoardItem("박다현", "010-2222-2222"))
        itemList.add(BoardItem("송라현", "010-3333-3333"))
        itemList.add(BoardItem("최마현", "010-4444-4444"))
        itemList.add(BoardItem("이바현", "010-5555-5555"))
        itemList.add(BoardItem("옹사현", "010-6666-6666"))
        itemList.add(BoardItem("차아현", "010-7777-7777"))
        itemList.add(BoardItem("강자현", "010-8888-8888"))
        itemList.add(BoardItem("현차현", "010-9999-9999"))
        itemList.add(BoardItem("장카현", "010-1234-5678"))
        itemList.add(BoardItem("하타현", "010-9876-5432"))
        itemList.add(BoardItem("마파현", "010-1213-1415"))
        itemList.add(BoardItem("정하현", "010-1617-1819"))
        itemList.add(BoardItem("김가현", "010-0000-0000"))
        itemList.add(BoardItem("임나현", "010-1111-1111"))
        itemList.add(BoardItem("박다현", "010-2222-2222"))
        itemList.add(BoardItem("송라현", "010-3333-3333"))
        itemList.add(BoardItem("최마현", "010-4444-4444"))
        itemList.add(BoardItem("이바현", "010-5555-5555"))
        itemList.add(BoardItem("옹사현", "010-6666-6666"))
        itemList.add(BoardItem("차아현", "010-7777-7777"))
        itemList.add(BoardItem("강자현", "010-8888-8888"))
        itemList.add(BoardItem("현차현", "010-9999-9999"))
        itemList.add(BoardItem("장카현", "010-1234-5678"))
        itemList.add(BoardItem("하타현", "010-9876-5432"))
        itemList.add(BoardItem("마파현", "010-1213-1415"))
        itemList.add(BoardItem("정하현", "010-1617-1819"))
        // 여기서 문제. 장카현 아래로는 스크롤이 안된다. 이건 constraint의 문제인가?
        // 네비게이션 바로 가려져서 그런건가? 음 그것도 아닌 것 같기도 하고... 나중에 해결하자


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

}