package com.ssafy.intagral.ui.common.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.R
import com.ssafy.intagral.data.PostItem
import com.ssafy.intagral.data.ProfileDetail
import com.ssafy.intagral.data.ProfileType
import com.ssafy.intagral.databinding.FragmentProfilePageBinding
import com.ssafy.intagral.databinding.FragmentUserProfileBinding
import com.ssafy.intagral.ui.common.post.PostAdapter
import com.ssafy.intagral.ui.home.HomeFragment

private const val ARG_PARAM1 = "profileType" //user or hashtag
private const val ARG_PARAM2 = "data" //profile data

class ProfilePageFragment : Fragment() {
    private var param1: ProfileType? = ProfileType.user //default user
    private var param2: String? = null //TODO: response json -> ProfileDetail로 변경 어디서할지 생각해보기

    //post list
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private var postList = mutableListOf<PostItem>()

    private lateinit var binding: FragmentProfilePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as ProfileType
            param2 = it.getString(ARG_PARAM2)
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

        //TODO: param2에서 필요한 data 뽑아서 profile detail fragment 생성
        parentFragmentManager.beginTransaction().replace(R.id.profile_detail,ProfileDetailFragment.newInstance(
            inputDataParam1, inputDataParam2)).commit()

        //TODO: hashtag인지 user인지 따라서 게시글 목록 요청하는 API 호출
        for(i in 1..9){
            postList.add(PostItem(i, "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"))
        }

        //TODO: refactor to binding
        postRecyclerView = view.findViewById(R.id.profile_post_list)
        context?.also {
            postAdapter = PostAdapter(it, postList)
            postAdapter.onItemClickListener = object : PostAdapter.OnItemClickListener {
                override fun onClick(view: View, position: Int) {
                    //TODO: post detail page로 이동
                    Toast.makeText(it,"listener : $position", Toast.LENGTH_SHORT).show()
                }
            }
            postRecyclerView.apply {
                adapter = postAdapter
                layoutManager = GridLayoutManager(it,3)
            }
        }

        //test dummy button
        view.findViewById<Button>(R.id.temporary_btn1).setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.profile_detail,ProfileDetailFragment.newInstance(
                ProfileType.hashtag, inputDataParam3)).commit()
        }
        view.findViewById<Button>(R.id.temporary_btn2).setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.profile_detail,ProfileDetailFragment.newInstance(
                ProfileType.user, inputDataParam4)).commit()
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ProfileType, param2: String) =
            ProfilePageFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

//test input data
val inputDataParam1:ProfileType = ProfileType.user
val inputDataParam2 = ProfileDetail(ProfileType.user, "yuyeon", "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png",
13,false,123,2,"dsfkdsjflkds")
val inputDataParam3 = ProfileDetail(ProfileType.hashtag, "yuyeon", "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png",
10, true)
val inputDataParam4 = ProfileDetail(ProfileType.user, "yuyeo2222n", "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png",
    10, true)