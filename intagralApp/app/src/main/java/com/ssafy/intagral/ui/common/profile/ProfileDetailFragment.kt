package com.ssafy.intagral.ui.common.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.ProfileDetail
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.viewmodel.ProfileDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "profileType"
private const val ARG_PARAM2 = "profileData"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProfileDetailFragment : Fragment() {
    private var param1: ProfileType = ProfileType.user //default user
    private var param2: ProfileDetail? = null
    private lateinit var binding: Any // TODO: 동적으로 type 설정 가능한지 확인
    private val profileDetailViewModel: ProfileDetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as ProfileType
            param2 = it.getSerializable(ARG_PARAM2) as ProfileDetail
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view :View
        if(param1.ordinal == 0) {
            // user profile
            view = inflater.inflate(R.layout.fragment_user_profile, container, false)
            val button: Button = view.findViewById<Button>(R.id.profile_detail_btn)
            if(param2?.name == "goodman") {
                //TODO: 조건문 본인일 때로 수정
                button.apply {
                    text = "setting"
                    setOnClickListener {
                        var btn = this as Button
                        if(btn.text.toString() == "setting"){
                            profileDetailViewModel.getEditStatus().value = ProfileDetailViewModel.EditStatus.ACTIVE
                        } else if(btn.text.toString() == "done") {
                            val regex = """\s""".toRegex()
                            var name = view.findViewById<TextInputLayout>(R.id.input_nickname).editText!!.text.toString()
                            name = regex.replace(name, "")
                            val intro = view.findViewById<TextInputLayout>(R.id.input_intro).editText!!.text.toString().trim()

                            if(name != ""){
                                profileDetailViewModel.editProfileData(name, intro)
                            }
                        }
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
            Glide.with(view.context).load(param2?.profileImg ?:"https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/car-967387__480.webp")
                .into(view.findViewById(R.id.user_profile_img))
            //TODO: follow 목록 페이지로 이동하는 리스너 등록

            //follower
            view.findViewById<TextView>(R.id.follower_cnt).text = param2?.follower?.toString() ?: "0"
            view.findViewById<LinearLayout>(R.id.follower_btn).apply {
                setOnClickListener {
                    Toast.makeText(it.context, "ddd", Toast.LENGTH_SHORT).show()
                }
            }
            //following
            view.findViewById<TextView>(R.id.following_cnt).text = param2?.following?.toString() ?: "0"
            view.findViewById<LinearLayout>(R.id.following_btn).apply {
                setOnClickListener {
                    Toast.makeText(it.context, "dddddd", Toast.LENGTH_SHORT).show()
                }
            }
            //hashtag
            view.findViewById<TextView>(R.id.hashtag_cnt).text = param2?.hashtag?.toString() ?: "0"
            view.findViewById<LinearLayout>(R.id.hashtag_btn).apply {
                setOnClickListener {
                    Toast.makeText(it.context, "ddddddddddd", Toast.LENGTH_SHORT).show()
                }
            }

            view.findViewById<TextView>(R.id.profile_detail_nickname).text = param2?.name ?: ""
            view.findViewById<TextView>(R.id.profile_detail_intro).text = param2?.intro ?:""
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
            Glide.with(view.context).load(param2?.profileImg ?:"https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png")
                .into(view.findViewById(R.id.hashtag_profile_img))
            //TODO: follow 목록 페이지로 이동하는 리스너 등록
            view.findViewById<TextView>(R.id.follower_cnt).text = param2?.follower?.toString() ?: "0"
            view.findViewById<TextView>(R.id.profile_detail_nickname).text = param2?.name ?: ""
            view.findViewById<TextView>(R.id.profile_detail_intro).text = param2?.intro ?:""
        }

        profileDetailViewModel.getEditStatus().observe(
            viewLifecycleOwner
        ){
            when(it){
                ProfileDetailViewModel.EditStatus.DUPLICATED_NAME -> {
                    val textLayout = view.findViewById<TextInputLayout>(R.id.input_nickname)
                    textLayout.error = null
                    textLayout.isErrorEnabled = false
                    textLayout.error = "닉네임 중복"
                }
                ProfileDetailViewModel.EditStatus.INAVTIVE -> {
                    view.findViewById<Button>(R.id.profile_detail_btn).text = "setting"
                    view.findViewById<LinearLayout>(R.id.profile_info_view_container).visibility = View.VISIBLE
                    view.findViewById<LinearLayout>(R.id.profile_info_edit_container).visibility = View.GONE
                }
                ProfileDetailViewModel.EditStatus.ERROR -> {
                    profileDetailViewModel.getEditStatus().value = ProfileDetailViewModel.EditStatus.INAVTIVE
                }
                ProfileDetailViewModel.EditStatus.ACTIVE -> {
                    view.findViewById<Button>(R.id.profile_detail_btn).text = "done"
                    view.findViewById<LinearLayout>(R.id.profile_info_view_container).visibility = View.GONE
                    view.findViewById<LinearLayout>(R.id.profile_info_edit_container).visibility = View.VISIBLE

                    view.findViewById<TextInputLayout>(R.id.input_nickname).editText!!.setText(profileDetailViewModel.getProfileDetail().value?.name ?: "")
                    view.findViewById<TextInputLayout>(R.id.input_intro).editText!!.setText(profileDetailViewModel.getProfileDetail().value?.intro ?: "")
                }
                else -> {

                }
            }
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