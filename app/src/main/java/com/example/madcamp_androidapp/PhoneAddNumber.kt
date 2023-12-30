package com.example.madcamp_androidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

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
        val BackButton: Button = findViewById(R.id.btn_back)
        BackButton.setOnClickListener {
            finish()
        }

    }
}