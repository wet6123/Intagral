package com.ssafy.intagral

import android.app.Activity
import android.content.Context

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationBarView
import com.ssafy.intagral.data.model.ProfileDetail
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.databinding.ActivityMainMenuBinding
import com.ssafy.intagral.ui.common.profile.ProfilePageFragment
import com.ssafy.intagral.ui.hashtagPreset.PresetViewFragment
import com.ssafy.intagral.ui.home.HomeFragment
import com.ssafy.intagral.ui.home.ProfileSimpleListFragment
import com.ssafy.intagral.ui.home.SearchActivity
import com.ssafy.intagral.ui.home.SettingFragment
import com.ssafy.intagral.ui.upload.PhotoPicker
import com.ssafy.intagral.util.LiveSharedPreferences
import com.ssafy.intagral.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import java.io.*

@AndroidEntryPoint
class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainMenuBinding
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    lateinit var mModule : Module
    lateinit var classList : ArrayList<String>
    private val profileDetailViewModel: ProfileDetailViewModel by viewModels()
    private val profileSimpleViewModel: ProfileSimpleViewModel by viewModels()
    private val postListViewModel: PostListViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreference =
            getSharedPreferences("intagral", Context.MODE_PRIVATE)
        val liveSharedPreference = LiveSharedPreferences(sharedPreference)

        // Observer 달아주는 과정
        liveSharedPreference
            .getString("token", "")
            .observe(this, Observer<String> { result ->
                if(result == "EXPIRED"){
                    this@MainMenuActivity.finish()
                }
            })

        // asset init
        mModule = LiteModuleLoader.load(assetFilePath(this, "best.opti.ptl"))
        classList = arrayListOf()
        val br = BufferedReader(InputStreamReader(assets.open("classes.txt")))
        while (br.readLine().also{ classList.add(it)} != null) {}

        setContentView(R.layout.activity_main_menu)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        mGoogleSignInClient = this.let { GoogleSignIn.getClient(it, gso) }

        binding = ActivityMainMenuBinding.inflate(layoutInflater).apply {
            setContentView(root)
            menuBottomNavigation.setOnItemSelectedListener(BottomTabListener())

            menuTopToolbar.inflateMenu(R.menu.top_bar)  //setSupportActionBar
            menuTopToolbar.setOnMenuItemClickListener(TopBarListener())

            activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                if(it.resultCode== RESULT_OK) {
                    profileDetailViewModel.changeProfileDetail(it?.data?.getSerializableExtra("profileSimple") as ProfileSimpleItem)
                }
            }
            profileDetailViewModel.getProfileDetail().observe(this@MainMenuActivity){
                it?.also {
                    //post list init함수 호출
                    postListViewModel.initPage(it.type.toString(), 1, it.name)
                    supportFragmentManager.beginTransaction().replace(R.id.menu_frame_layout, ProfilePageFragment()).commit()
                }
            }

            profileSimpleViewModel.getProfileSimpleList().observe(this@MainMenuActivity){
                it?.also{
                    supportFragmentManager.beginTransaction().replace(R.id.menu_frame_layout, ProfileSimpleListFragment()).commit()
                }
            }
        }

        userViewModel.getMyInfo().observe(this@MainMenuActivity){
            IntagralApplication.prefs.nickname = it.nickname
        }
        loginViewModel.getIsLogin().observe(this@MainMenuActivity){
            if(!it){
                mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this) {
                        // 로그아웃 성공시 실행
                        // 로그아웃 이후의 이벤트들(토스트 메세지, 화면 종료)을 여기서 수행하면 됨
                        finish()
                    }
            }
        }
        userViewModel.getInfo()


        setHome()
    }

    private fun setHome() {
        postListViewModel.initPage("all", 1, null)
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
                    //TODO: change user name
                    var test = IntagralApplication.prefs.nickname
                    profileDetailViewModel.changeProfileDetail(ProfileSimpleItem(ProfileType.user, IntagralApplication.prefs.nickname!!, false, ""))
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
                    val intent = Intent(this@MainMenuActivity, SearchActivity::class.java)
                    activityResultLauncher.launch(intent)
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

    fun logout() {
        loginViewModel.logout()
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

    fun changeProfileDetail(index: Int, profileSimple: ProfileSimpleItem) {
        when(index) {
            1 -> {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken,0)
                profileDetailViewModel.changeProfileDetail(profileSimple)
            }
        }
    }
}

val dummyData: ProfileDetail = ProfileDetail(ProfileType.user, "yuyeon",
    200,false, "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/remove-background-before-qa1.png")
val dummyData2: ProfileDetail = ProfileDetail(ProfileType.user, "한유연", 200,false)