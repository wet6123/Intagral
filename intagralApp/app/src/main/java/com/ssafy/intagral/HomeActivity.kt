package com.ssafy.intagral

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.ssafy.intagral.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

//    private lateinit var userTextView : TextView
//    private lateinit var returnBtn : Button

    //viewBinding
    private lateinit var binding : ActivityHomeBinding

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
        val userEmail = intent.getStringExtra("user")
        binding.testUserEmailView.text = userEmail

        //돌아가기 버튼 클릭하면 MainActivity로 넘어간다
        binding.returnButton.setOnClickListener{

            //돌아가기 전 데이터 전달 가능
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("test_to_main","testIntent_to_main")
            setResult(Activity.RESULT_OK, intent)

            finish() //HomeActivity 종료
        }
    }
}