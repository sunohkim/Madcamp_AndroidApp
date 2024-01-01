package com.example.madcamp_androidapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.view.View

class PhoneAddNumber : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_add_number)

//        val BackButton: Button = findViewById(R.id.btn_back)
//        BackButton.setOnClickListener {
//            val intent = Intent(this, PhoneFragment::class.java)
//            startActivity(intent)
//            finish()
//        }

//        // 여기서 메인 리스트에 추가할 리스트를 만들고, PhoneFragment로 전달
//        val AddPhoneNum: Button = findViewById(R.id.btn_add)
//        val WannaAddName: EditText = findViewById(R.id.edit_name)
//        val WannaAddNum: EditText = findViewById(R.id.edit_number)
//        // val WannaIntent = Intent(this, PhoneFragment::class.java)
//        var bundle = Bundle()
//        var addfragment = PhoneFragment()
//        AddPhoneNum.setOnClickListener {
//            val NewPhoneNum: BoardItem = BoardItem(WannaAddName.text.toString(), WannaAddNum.text.toString())
//            val NewName = WannaAddName.text.toString()
//            val NewNum = WannaAddNum.text.toString()
//            Log.w("Backcheck", NewName)
//            Log.w("Backcheck,", NewNum)
//            //WannaIntent.putExtra("name", NewName)
//            //WannaIntent.putExtra("num", NewNum)
//
//            //supportFragmentManager.beginTransaction().replace(R.id.add_layout, addfragment).commit()
//
//            bundle.putString("name", NewName)
//            bundle.putString("num", NewNum)
//            addfragment.arguments = bundle
//
//            //activity?.supportFragmentManager!!.beginTransaction().replace(R.id., addfragment).commit()
//            //startActivity(WannaIntent)
//
//        }
        val AddPhoneNum: Button = findViewById(R.id.btn_add)
        val name: EditText = findViewById(R.id.edit_name)
        val num: EditText = findViewById(R.id.edit_number)

        //val phoneFragment = supportFragmentManager.findFragmentById(R.id.phone_frame) as PhoneFragment

//        AddPhoneNum.setOnClickListener {
//            val newname: String = name.text.toString()
//            val newnum: String = num.text.toString()
//
//            val fragment1 = PhoneFragment()
//            //supportFragmentManager.beginTransaction().replace(R.id.add_layout, fragment1).commit()
//
//            val bundle = Bundle()
//            bundle.putString("name", newname)
//            bundle.putString("num", newnum)
//
//            phoneFragment.updateData(bundle)
//
//            finish()
//
//
////            fragment1.arguments = bundle
////            supportFragmentManager.beginTransaction().replace(R.id.add_layout, fragment1).commit()
////            finish()
//
//        }
        AddPhoneNum.setOnClickListener {
            val newname: String = name.text.toString()
            val newnum: String = num.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("name", newname)
            resultIntent.putExtra("num", newnum)
            setResult(Activity.RESULT_OK, resultIntent)

            finish()
        }

        // 뒤로 가기 버튼
        val BackButton: Button = findViewById(R.id.btn_back)
        BackButton.setOnClickListener {
            finish()
        }

    }
}