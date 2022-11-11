package com.ssafy.intagral.ui.common.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.PostItem
import com.ssafy.intagral.data.model.ProfileDetail
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.databinding.FragmentProfilePageBinding
import com.ssafy.intagral.ui.common.post.PostAdapter
import com.ssafy.intagral.ui.common.post.PostListFragment

import com.ssafy.intagral.viewmodel.PostListViewModel

private const val ARG_PARAM1 = "profileType" //user or hashtag
private const val ARG_PARAM2 = "data" //profile data

class ProfilePageFragment : Fragment() {
    private var param1: ProfileType? = ProfileType.user //default user
    private var param2: ProfileDetail? = null //TODO: response json -> ProfileDetail로 변경 어디서할지 생각해보기

    private lateinit var binding: FragmentProfilePageBinding

    private val postListViewModel: PostListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as ProfileType
            param2 = it.getSerializable(ARG_PARAM2) as ProfileDetail
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//TODO: refactor to binding
/***
        binding = FragmentProfilePageBinding.inflate(layoutInflater).apply {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(this@ProfilePageFragment.id, ProfileDetailFragment.newInstance(
                    inputDataParam1, inputDataParam2)).commit()
        }
***/
        val view = inflater.inflate(R.layout.fragment_profile_page,container,false)


        //TODO: tag선택 따라서 PostListViewModel 변경
        postListViewModel.initPage(param1.toString(), 1, param2?.name)
        parentFragmentManager.beginTransaction().replace(R.id.fragment_profile_page_post_list, PostListFragment()).commit()

        //TODO: param2에서 필요한 data 뽑아서 profile detail fragment 생성
        parentFragmentManager.beginTransaction().replace(R.id.profile_detail,ProfileDetailFragment.newInstance(
            param1 ?:ProfileType.user, param2 ?: dummyProfile)).commit()

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ProfileType, param2: ProfileDetail) =
            ProfilePageFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                }
            }
    }
}

//test input data
val inputDataParam1: ProfileType = ProfileType.user
val inputDataParam2 = ProfileDetail(ProfileType.user, "yuyeon",
13,false, null, 123)
val inputDataParam3 = ProfileDetail(ProfileType.hashtag, "yuyeon", 1232, true)
val inputDataParam4 = ProfileDetail(ProfileType.user, "yuyeo2222n",
    10, true)

//dummy ProfileDetail
val dummyProfile = ProfileDetail(ProfileType.user, "yuyeon2",
    1024,true, null, 123, 17, "hi")