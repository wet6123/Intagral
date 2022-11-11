package com.ssafy.intagral.ui.common.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.ssafy.intagral.databinding.FragmentUserProfileBinding
import com.ssafy.intagral.viewmodel.ProfileDetailViewModel
import com.ssafy.intagral.viewmodel.ProfileSimpleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileDetailFragment: Fragment() {

    private lateinit var binding: FragmentUserProfileBinding

    private val profileSimpleViewModel: ProfileSimpleViewModel by activityViewModels()
    private val profileDetailViewModel: ProfileDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false).apply {
            //TODO: 본인인지 아닌지
            // profile null일 때
            if (profileDetailViewModel.getProfileDetail().value?.name == "goodman") {
                profileDetailBtn.apply {
                    text = "setting"
                    setOnClickListener {
                        if (this.text == "setting") {
                            profileDetailViewModel.getEditStatus().value = ProfileDetailViewModel.EditStatus.ACTIVE
                        } else if (this.text == "done") {
                            val regex = """\s""".toRegex()
                            var name = inputNickname.editText!!.text.toString()
                            name = regex.replace(name, "")
                            val intro = inputIntro.editText!!.text.toString().trim()

                            if (name != "") {
                                profileDetailViewModel.editProfileData(name, intro)
                            }
                        }
                    }
                }
            } else {
                profileDetailBtn.apply {
                    text = if (profileDetailViewModel.getProfileDetail().value?.isFollow ?:false) "UNFOLLOW" else "FOLLOW"
                    setOnClickListener {
                        //TODO: follow API 호출
                        Toast.makeText(it.context, "follow API 호출", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            context?.let {
                Glide.with(it).load(
                    profileDetailViewModel.getProfileDetail().value?.profileImg
                        ?: "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/car-967387__480.webp"
                ).into(this.userProfileImg)
                //TODO: follow 목록 페이지로 이동하는 리스너 등록
            }

            followerCnt.text =
                profileDetailViewModel.getProfileDetail().value?.follower?.toString() ?: "0"
            followingCnt.text =
                profileDetailViewModel.getProfileDetail().value?.following?.toString() ?: "0"
            hashtagCnt.text =
                profileDetailViewModel.getProfileDetail().value?.hashtag?.toString() ?: "0"

            followerBtn.apply {
                setOnClickListener {
                    profileDetailViewModel.getProfileDetail().value?.name.let {
                        // observe from MainMenuActivity
                        // TODO: create and manage status ex. init, create, ..
                        profileSimpleViewModel.getOnesFollowList(
                            "follower",
                            profileDetailViewModel.getProfileDetail().value!!.name
                        )
                    }
                }
            }

            followingBtn.apply {
                setOnClickListener {
                    profileDetailViewModel.getProfileDetail().value?.name.let {
                        profileSimpleViewModel.getOnesFollowList(
                            "following",
                            profileDetailViewModel.getProfileDetail().value!!.name
                        )
                    }
                }
            }

            hashtagBtn.apply {
                setOnClickListener {
                    profileDetailViewModel.getProfileDetail().value?.name.let {
                        profileSimpleViewModel.getOnesFollowList(
                            "hashtag",
                            profileDetailViewModel.getProfileDetail().value!!.name
                        )
                    }
                }
            }

            profileDetailNickname.text = profileDetailViewModel.getProfileDetail().value?.name
            profileDetailIntro.text = profileDetailViewModel.getProfileDetail().value?.intro

            profileDetailViewModel.getEditStatus().observe(
                viewLifecycleOwner
            ) {
                when (it) {
                    ProfileDetailViewModel.EditStatus.DUPLICATED_NAME -> {
                        val textLayout = inputNickname
                        textLayout.error = null
                        textLayout.isErrorEnabled = false
                        textLayout.error = "닉네임 중복"
                    }
                    ProfileDetailViewModel.EditStatus.INAVTIVE -> {
                        profileDetailBtn.text = "setting"
                        profileInfoViewContainer.visibility = View.VISIBLE
                        profileInfoEditContainer.visibility = View.GONE
                    }
                    ProfileDetailViewModel.EditStatus.ERROR -> {
                        profileDetailViewModel.getEditStatus().value =
                            ProfileDetailViewModel.EditStatus.INAVTIVE
                    }
                    ProfileDetailViewModel.EditStatus.ACTIVE -> {
                        profileDetailBtn.text = "done"
                        profileInfoViewContainer.visibility = View.GONE
                        profileInfoEditContainer.visibility = View.VISIBLE

                        inputNickname.editText!!.setText(
                            profileDetailViewModel.getProfileDetail().value?.name ?: ""
                        )
                        inputIntro.editText!!.setText(
                            profileDetailViewModel.getProfileDetail().value?.intro ?: ""
                        )
                    }
                    else -> {

                    }
                }
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        profileDetailViewModel.getEditStatus().value = null
    }
}