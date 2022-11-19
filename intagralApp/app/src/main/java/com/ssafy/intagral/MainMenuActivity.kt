package com.ssafy.intagral

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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationBarView
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.databinding.ActivityMainMenuBinding
import com.ssafy.intagral.ui.common.profile.ProfilePageFragment
import com.ssafy.intagral.ui.common.profile.ProfileSimpleListFragment
import com.ssafy.intagral.ui.hashtagPreset.PresetViewFragment
import com.ssafy.intagral.ui.home.HomeFragment
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
    private val userViewModel: UserViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val filterTagViewModel: FilterTagViewModel by viewModels()

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
        mModule = LiteModuleLoader.load(assetFilePath(this, "bestV2.opti.ptl"))
        classList = arrayListOf()
        val br = BufferedReader(InputStreamReader(assets.open("classesV2.txt")))
        while (br.readLine().also{ if(it != null) classList.add(it)} != null) {}

        setContentView(R.layout.activity_main_menu)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        mGoogleSignInClient = this.let { GoogleSignIn.getClient(it, gso) }

        binding = ActivityMainMenuBinding.inflate(layoutInflater).apply {
            setContentView(root)
            menuBottomNavigation.setOnItemSelectedListener(BottomTabListener())

            menuTopToolbar.inflateMenu(R.menu.top_bar)  //setSupportActionBar
            menuTopToolbar.setOnMenuItemClickListener(TopBarListener())

            topIntagralLogo.setOnClickListener{
                supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                supportFragmentManager.beginTransaction().replace(R.id.menu_frame_layout, HomeFragment()).commit()
            }

            activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                if(it.resultCode== RESULT_OK) {
                    profileDetailViewModel.changeProfileDetail(it?.data?.getSerializableExtra("profileSimple") as ProfileSimpleItem)
                }
            }
            profileDetailViewModel.getProfileDetail().observe(this@MainMenuActivity){
                it?.also {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction().replace(R.id.menu_frame_layout, ProfilePageFragment()).commit()
                }
            }

            // this observer is used only in one's following or follower page
            profileSimpleViewModel.getProfileSimpleList().observe(this@MainMenuActivity){
                it?.also{
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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

    fun setHome() {
        binding.menuBottomNavigation.selectedItemId = R.id.nav_home
    }

    inner class BottomTabListener : NavigationBarView.OnItemSelectedListener{
        override fun onNavigationItemSelected(item: MenuItem): Boolean {

            val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
            when(item?.itemId){
                R.id.nav_home -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    transaction.replace(R.id.menu_frame_layout, HomeFragment()).commit()
                }
                R.id.nav_upload -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    transaction.replace(R.id.menu_frame_layout, PhotoPicker.newInstance()).commit()
                }
                R.id.nav_hashtag -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    transaction.replace(R.id.menu_frame_layout, PresetViewFragment.newInstance()).commit()
                }
                R.id.nav_mypage -> {
                    profileDetailViewModel.changeProfileDetail(ProfileSimpleItem(ProfileType.user, IntagralApplication.prefs.nickname!!, false, ""))
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
                R.id.toolbar_search_icon -> {
                    val intent = Intent(this@MainMenuActivity, SearchActivity::class.java)
                    activityResultLauncher.launch(intent)
                }
                R.id.toolbar_setting -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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
