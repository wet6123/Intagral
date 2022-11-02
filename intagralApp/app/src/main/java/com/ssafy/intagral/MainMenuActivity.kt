package com.ssafy.intagral

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationBarView
import com.ssafy.intagral.databinding.ActivityMainMenuBinding
import com.ssafy.intagral.ui.home.HomeFragment
import com.ssafy.intagral.ui.home.SearchFragment
import com.ssafy.intagral.ui.upload.PhotoPicker

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainMenuBinding
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        mGoogleSignInClient = this.let { GoogleSignIn.getClient(it, gso) }

        binding = ActivityMainMenuBinding.inflate(layoutInflater).apply {
            setContentView(root)
            menuBottomNavigation.setOnItemSelectedListener(BottomTabListener())

            menuTopToolbar.inflateMenu(R.menu.top_bar)  //setSupportActionBar
            menuTopToolbar.setOnMenuItemClickListener(TopBarListener())

            val searchItem = menuTopToolbar.menu.findItem(R.id.toolbar_search_icon).actionView as SearchView
            searchItem.setOnQueryTextListener(SearchListener())

        }

        setHome()
    }
    private fun setHome() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.menu_frame_layout, HomeFragment()).commit()
    }

    inner class BottomTabListener : NavigationBarView.OnItemSelectedListener{
        override fun onNavigationItemSelected(item: MenuItem): Boolean {

            val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
            when(item?.itemId){
                R.id.nav_home -> {
                    println("home selected!")
                    setHome()
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

    inner class TopBarListener : Toolbar.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.toolbar_logo -> { // redirect home
                    val intent = intent // ?
                    finishAffinity()
                    startActivity(intent)
                }
                R.id.toolbar_search_icon -> {

                    supportFragmentManager.beginTransaction().replace(R.id.menu_frame_layout, SearchFragment()).commit()
                }
                R.id.toolbar_tmpLogout -> {
                    logout()
                    finish()
                    val intent = Intent(this@MainMenuActivity, MainActivity::class.java) // TODO: redirect mainActivity
                    startActivity(intent)
                }
                else -> {

                }
            }
            return true
        }
    }

    inner class SearchListener : OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            Toast.makeText(this@MainMenuActivity,"on query text submit", Toast.LENGTH_SHORT).show()
            return false
        }
        override fun onQueryTextChange(newText: String?): Boolean {
            Toast.makeText(this@MainMenuActivity,"on query text change", Toast.LENGTH_SHORT).show()
            return false
        }
    }
    private fun logout() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // 로그아웃 성공시 실행
                // 로그아웃 이후의 이벤트들(토스트 메세지, 화면 종료)을 여기서 수행하면 됨
            }
    }
}