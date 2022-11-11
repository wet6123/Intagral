package com.ssafy.intagral.ui.common.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.ssafy.intagral.databinding.FragmentHashtagProfileBinding
import com.ssafy.intagral.viewmodel.ProfileDetailViewModel
import com.ssafy.intagral.viewmodel.ProfileSimpleViewModel

class HashtagProfileDetailFragment: Fragment() {
    private lateinit var binding: FragmentHashtagProfileBinding

    private val profileSimpleViewModel: ProfileSimpleViewModel by activityViewModels()
    private val profileDetailViewModel: ProfileDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHashtagProfileBinding.inflate(inflater, container, false).apply {
            profileDetailBtn.apply {
                text = if(profileDetailViewModel.getProfileDetail().value?.isFollow ?:false) "UNFOLLOW" else "FOLLOW"
                setOnClickListener {
                    //TODO: follow API 호출
                    Toast.makeText(this.context, "follow API 호출", Toast.LENGTH_SHORT).show()
                }
            }
            context?.let {
                Glide.with(it).load(profileDetailViewModel.getProfileDetail().value?.profileImg ?: "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png")
                    .into(hashtagProfileImg)
            }
            followerCnt.text =
                profileDetailViewModel.getProfileDetail().value?.follower?.toString() ?: "0"

            profileDetailFollower.apply {
                setOnClickListener {
                    profileDetailViewModel.getProfileDetail().value?.name.let {
                        // observe from MainMenuActivity
                        // TODO: create and manage status ex. init, create, ..
                        profileSimpleViewModel.getHashtagFollowerList(
                            profileDetailViewModel.getProfileDetail().value!!.name
                        )
                    }
                }
            }
            profileDetailNickname.text = profileDetailViewModel.getProfileDetail().value?.name
            profileDetailIntro.text = profileDetailViewModel.getProfileDetail().value?.intro
        }
        return binding.root
    }
}