package com.ssafy.intagral

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.ssafy.intagral.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {

//    private lateinit var userTextView : TextView
//    private lateinit var returnBtn : Button

    //viewBinding
    private lateinit var binding : ActivityHomeBinding
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_home)

        //findViewById 이용
//        userTextView = findViewById(R.id.testUserEmailView)
//        returnBtn = findViewById(R.id.returnButton)

        //viewBinding 이용
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //전달받은 인텐트에서 extra 정보 확인 후 화면에 출력
        val userEmail = intent.getStringExtra("userEmail")
        binding.testUserEmailView.text = userEmail
        binding.testUserNameView.text = intent.getStringExtra("userName")


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        mGoogleSignInClient = this.let { GoogleSignIn.getClient(it, gso) }

        //돌아가기 버튼 클릭하면 MainActivity로 넘어간다
        binding.returnButton.setOnClickListener{
            logout()
            //revokeAccess()
            finish() //HomeActivity 종료
        }
    }
    private fun logout() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // 로그아웃 성공시 실행
                // 로그아웃 이후의 이벤트들(토스트 메세지, 화면 종료)을 여기서 수행하면 됨
            }
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(this) {
                // ...
            }
    }
}