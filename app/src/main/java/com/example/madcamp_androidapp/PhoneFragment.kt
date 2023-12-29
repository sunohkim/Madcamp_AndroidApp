package com.example.madcamp_androidapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhoneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhoneFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_phone, container, false)
        val rv_board = view.findViewById<RecyclerView>(R.id.rv_board)
        //rv_board.layoutManager = LinearLayoutManager(context)
        val itemList = ArrayList<BoardItem>()
        
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

        val boardAdapter = BoardAdapter(itemList)
        rv_board.adapter = boardAdapter
        rv_board.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhoneFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhoneFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}