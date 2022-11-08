package com.ssafy.intagral.ui.common.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.ProfileDetail
import com.ssafy.intagral.data.model.ProfileType

private const val ARG_PARAM1 = "profileType"
private const val ARG_PARAM2 = "profileData"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileDetailFragment : Fragment() {
    private var param1: ProfileType = ProfileType.user //default user
    private var param2: ProfileDetail? = null
    private lateinit var binding: Any // TODO: 동적으로 type 설정 가능한지 확인
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as ProfileType
            param2 = it.getSerializable(ARG_PARAM2) as ProfileDetail //TODO: response json -> ProfileDetail로 변경 어디서할지 생각해보기
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view :View
        if(param1.ordinal == 0) {
            // user profile
            view = inflater.inflate(R.layout.fragment_user_profile, container, false)
            val button: Button = view.findViewById<Button>(R.id.profile_detail_btn)
            if(param2?.name == "yuyeon") {
                //TODO: 조건문 본인일 때로 수정
                button.apply {
                    text = "setting"
                    setOnClickListener {
                        //TODO: profile setting Fragment로 교환
                        Toast.makeText(view.context, "setting fragment로 이동", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                button.apply {
                    text = if(param2?.isFollow == true) "UNFOLLOW" else "FOLLOW"
                    setOnClickListener {
                        //TODO: follow API 호출
                        Toast.makeText(view.context, "follow API 호출", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            Glide.with(view.context).load("https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/car-967387__480.webp")
                .into(view.findViewById(R.id.user_profile_img))
            //TODO: follow 목록 페이지로 이동하는 리스너 등록
            view.findViewById<TextView>(R.id.follower_cnt).text = param2?.follower?.toString() ?: "0"
            view.findViewById<TextView>(R.id.following_cnt).text = param2?.following?.toString() ?: "0"
            view.findViewById<TextView>(R.id.hashtag_cnt).text = param2?.hashtag?.toString() ?: "0"
        } else {
            // hashtag fragment
            view = inflater.inflate(R.layout.fragment_hashtag_profile, container, false)
            val button: Button = view.findViewById<Button>(R.id.profile_detail_btn)
            button.apply {
                text = if(param2?.isFollow == true) "UNFOLLOW" else "FOLLOW"
                setOnClickListener {
                    //TODO: follow API 호출
                    Toast.makeText(view.context, "follow API 호출", Toast.LENGTH_SHORT).show()
                }
            }
            Glide.with(view.context).load("https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png")
                .into(view.findViewById(R.id.hashtag_profile_img))
            //TODO: follow 목록 페이지로 이동하는 리스너 등록
            view.findViewById<TextView>(R.id.follower_cnt).text = param2?.follower?.toString() ?: "0"
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ProfileType, param2: ProfileDetail) =
            ProfileDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                }
            }
    }
}