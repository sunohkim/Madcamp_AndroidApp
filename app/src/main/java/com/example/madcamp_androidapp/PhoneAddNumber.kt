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
import android.widget.Toast

class PhoneAddNumber : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_add_number)

    
        val AddPhoneNum: Button = findViewById(R.id.btn_add) // 추가하기 버튼
        val name: EditText = findViewById(R.id.edit_name) // 입력한 이름
        val num: EditText = findViewById(R.id.edit_number) // 입력한 번호

        // 추가하기 버튼을 눌렀을 때, PhoneFragment로 입력한 이름과 번호를 전달하는 과정
        AddPhoneNum.setOnClickListener {
            val newname: String = name.text.toString()
            val newnum: String = num.text.toString()
            if (!newname.isNullOrEmpty() && !newnum.isNullOrEmpty()) {

                val resultIntent = Intent() // Intent를 이용해서 전달
                resultIntent.putExtra("name", newname)
                resultIntent.putExtra("num", newnum)

                // Result가 나왔음을 setResult()를 통해 PhoneFragment의 onActivityResult에 전달
                setResult(Activity.RESULT_OK, resultIntent)

                finish() // Activity 창 닫기
            }
            else {
                Toast.makeText(this, "이름, 전화번호 정보를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 뒤로 가기 버튼
        val BackButton: Button = findViewById(R.id.btn_back)
        BackButton.setOnClickListener {
            finish()
        }

    }
}