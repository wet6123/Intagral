package com.ssafy.intagral.ui.common.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.databinding.FragmentProfilePageBinding
import com.ssafy.intagral.ui.common.post.PostListFragment
import com.ssafy.intagral.viewmodel.PostListViewModel
import com.ssafy.intagral.viewmodel.ProfileDetailViewModel


class ProfilePageFragment : Fragment() {

    private lateinit var binding: FragmentProfilePageBinding

    private val postListViewModel: PostListViewModel by activityViewModels()
    private val profileDetailViewModel: ProfileDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfilePageBinding.inflate(inflater, container, false)

        postListViewModel.initPage(profileDetailViewModel.getProfileDetail().value!!.type.toString(), 1, profileDetailViewModel.getProfileDetail().value!!.name)
        parentFragmentManager.beginTransaction().replace(R.id.fragment_profile_page_post_list, PostListFragment()).commit()

        if(profileDetailViewModel.getProfileDetail().value!!.type == ProfileType.user){
            parentFragmentManager.beginTransaction().replace(R.id.profile_detail, UserProfileDetailFragment()).commit()
        } else {
            parentFragmentManager.beginTransaction().replace(R.id.profile_detail, HashtagProfileDetailFragment()).commit()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        parentFragmentManager.findFragmentById(R.id.profile_detail)?.let {
            parentFragmentManager.beginTransaction().remove(it).commitAllowingStateLoss()
        }
    }
}
