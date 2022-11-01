package com.ssafy.intagral

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationBarView
import com.ssafy.intagral.data.ProfileType
import com.ssafy.intagral.databinding.ActivityMainMenuBinding
import com.ssafy.intagral.ui.common.profile.ProfilePageFragment
import com.ssafy.intagral.ui.hashtagPreset.PresetViewFragment
import com.ssafy.intagral.ui.home.HomeFragment
import com.ssafy.intagral.ui.home.SearchFragment
import com.ssafy.intagral.ui.home.SettingFragment
import com.ssafy.intagral.ui.upload.PhotoPicker
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainMenuBinding
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    lateinit var mModule : Module

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mModule = LiteModuleLoader.load(assetFilePath(this, "best.opti.ptl"))

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
//TODO : Search Fragment에서 Home으로 뒤로가기 했을 때 키보드 풀기
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
                    transaction.replace(R.id.menu_frame_layout, PresetViewFragment.newInstance()).commit()
                }
                R.id.nav_mypage -> {
                    transaction.replace(R.id.menu_frame_layout, ProfilePageFragment.newInstance(ProfileType.user,"tmp")).commit()
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
                R.id.toolbar_setting -> {
                    supportFragmentManager.beginTransaction().replace(R.id.menu_frame_layout, SettingFragment()).commit()
                }
                else -> {

                }
            }
            return true
        }
    }

    inner class SearchListener : OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            supportFragmentManager.setFragmentResult("search text", bundleOf("input text" to query))
            return true
        }
        override fun onQueryTextChange(newText: String?): Boolean {
            supportFragmentManager.setFragmentResult("search text", bundleOf("input text" to newText))
            return true
        }
    }
    fun logout() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // 로그아웃 성공시 실행
                // 로그아웃 이후의 이벤트들(토스트 메세지, 화면 종료)을 여기서 수행하면 됨
                finish()
            }
    }


    /**
     * asset
     */
    @Throws(IOException::class)
    fun assetFilePath(context: Context, assetName: String?): String? {
        val file = File(context.filesDir, assetName)
        if (file.exists() && file.length() > 0) {
            return file.absolutePath
        }
        context.assets.open(assetName!!).use { `is` ->
            FileOutputStream(file).use { os ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (`is`.read(buffer).also { read = it } != -1) {
                    os.write(buffer, 0, read)
                }
                os.flush()
            }
            return file.absolutePath
        }
    }
}