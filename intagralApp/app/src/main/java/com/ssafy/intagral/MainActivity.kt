package com.ssafy.intagral

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.ssafy.intagral.databinding.ActivityMainBinding
import com.ssafy.intagral.util.PreferenceUtil
import com.ssafy.intagral.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    private val loginViewModel : LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        loginViewModel.getAuthToken().observe(this@MainActivity){
            IntagralApplication.prefs.token = it;
            updateUI();
        }
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account == null || IntagralApplication.prefs.token == "EXPIRED"){
            IntagralApplication.prefs.token = ""
            println("onStart account null")
            val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
            signInButton.setSize(SignInButton.SIZE_STANDARD)
            signInButton.setOnClickListener { signIn() }
        } else {
            println("onStart account not null")
            updateUI()
        }

    }

    private fun updateUI(){
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent) //단방향 이동
    }

    @Override
    fun onClick(v: View) {
        when (v.getId()) {
            R.id.sign_in_button -> signIn()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) { //originally 1
            val data: Intent? = result.data
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            loginViewModel.login(account.idToken!!)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "login 실패", Toast.LENGTH_SHORT).show()
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
        }
    }
}
