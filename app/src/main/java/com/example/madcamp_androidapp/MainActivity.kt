package com.example.madcamp_androidapp

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.madcamp_androidapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 앱 실행 시 가장 먼저 나타나는 화면
        setFragment("phone_fragment", PhoneFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_phone -> setFragment("phone_fragment", PhoneFragment())
                R.id.navigation_gallery -> setFragment("gallery_fragment", GalleryFragment())
                R.id.navigation_game -> setFragment("game_fragment", GameFragment())
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val phone = manager.findFragmentByTag("phone_fragment")
        val gallery = manager.findFragmentByTag("gallery_fragment")
        val game = manager.findFragmentByTag("game_fragment")

        // 비활성화
        if (phone != null){
            fragTransaction.hide(phone)
        }
        if (gallery != null){
            fragTransaction.hide(gallery)
        }
        if (game != null) {
            fragTransaction.hide(game)
        }

        // 활성화
        if (tag == "phone_fragment") {
            if (phone!=null){
                fragTransaction.show(phone)
            }
        }
        else if (tag == "gallery_fragment") {
            if (gallery != null) {
                fragTransaction.show(gallery)
            }
        }
        else if (tag == "game_fragment"){
            if (game != null){
                fragTransaction.show(game)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}