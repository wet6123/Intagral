package com.ssafy.intagral

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {

//    private lateinit var binding : ActivityMainBinding
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/** ssafy 필기 : intent 가지고 다른 Activity로 이동
        signInButton.setOnClickListener{
            val intent = Intent(this, SubActivity::class.java)
            intent.putExtra("name","ssafy")
            startActivity(intent) //단방향 이동
        }
**/

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null) {
            println("onStart account not null")
            updateUI(account)
        } else {
            println("onStart account null")
            val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
            signInButton.setSize(SignInButton.SIZE_STANDARD)

/** ssafy 필기 : activity에 intent 담아 양방향 호출
            signInButton.setOnClickListener{
                val email = "hyy0416@naver.com"
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("user",email)
                //startActivity(intent) 단방향

                //양방향 호출
                subActivityLauncher.launch(intent)
            }
**/
            signInButton.setOnClickListener { signIn() }

        }
    }

    private fun updateUI(account: GoogleSignInAccount){
        println("updateUI 시작")
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("userEmail",account.email)
        intent.putExtra("userName",account.displayName)
        startActivity(intent) //단방향 이동
    }

/** ssafy 필기: 변수명 빼고 통째로 외워도 되는 코드
    private val subActivityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.resultCode == Activity.RESULT_OK) {
            val intent = it.data //subActivity에서 보낸 intent 객체
            val returnValue = intent!!.getStringExtra("test_to_main")
            Toast.makeText(this,returnValue, Toast.LENGTH_SHORT).show()
        }
    }
**/

    @Override
    fun onClick(v: View) {
        when (v.getId()) {
            R.id.sign_in_button -> signIn()
        }
    }

    private fun signIn() {
        println("signIn 함수 시작")
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        println("result launcher 시작")
        if (result.resultCode == RESULT_OK) { //originally 1
            val data: Intent? = result.data
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            println("handle SignIn Result 함수 시작")
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
        }
    }

}