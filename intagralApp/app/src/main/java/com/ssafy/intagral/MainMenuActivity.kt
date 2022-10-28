package com.ssafy.intagral

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.ssafy.intagral.databinding.ActivityMainMenuBinding
import com.ssafy.intagral.ui.upload.PhotoPicker

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        binding = ActivityMainMenuBinding.inflate(layoutInflater).apply {
            setContentView(root)
            menuBottomNavigation.setOnItemSelectedListener(BottomTabListener())
        }
    }

    inner class BottomTabListener : NavigationBarView.OnItemSelectedListener{
        override fun onNavigationItemSelected(item: MenuItem): Boolean {

            val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
            when(item?.itemId){
                R.id.nav_home -> {
                    println("home selected!")
                }
                R.id.nav_upload -> {
                    println("upload selected!")
                    transaction.replace(R.id.menu_frame_layout, PhotoPicker.newInstance()).commit()
                }
                R.id.nav_hashtag -> {
                    println("hashtag selected!")
                }
                R.id.nav_mypage -> {
                    println("mypage selected!")
                }
                else -> {

                }
            }

            return true
        }
    }
}