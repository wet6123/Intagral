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
import com.ssafy.intagral.viewmodel.ProfileDetailViewModel


class ProfilePageFragment : Fragment() {

    private lateinit var binding: FragmentProfilePageBinding

    private val profileDetailViewModel: ProfileDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfilePageBinding.inflate(inflater, container, false)

        childFragmentManager.beginTransaction().replace(R.id.fragment_profile_page_post_list, PostListFragment.newInstance(
            profileDetailViewModel.getProfileDetail().value!!.type.toString(),
            profileDetailViewModel.getProfileDetail().value!!.name
        )).commit()

        if(profileDetailViewModel.getProfileDetail().value!!.type == ProfileType.user){
            childFragmentManager.beginTransaction().replace(R.id.profile_detail, UserProfileDetailFragment()).commit()
        } else {
            childFragmentManager.beginTransaction().replace(R.id.profile_detail, HashtagProfileDetailFragment()).commit()
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
