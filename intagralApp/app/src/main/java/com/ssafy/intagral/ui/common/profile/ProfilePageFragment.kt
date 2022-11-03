package com.ssafy.intagral.ui.common.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.R
import com.ssafy.intagral.data.PostItem
import com.ssafy.intagral.data.ProfileDetail
import com.ssafy.intagral.databinding.FragmentProfilePageBinding
import com.ssafy.intagral.databinding.FragmentUserProfileBinding
import com.ssafy.intagral.ui.common.post.PostAdapter
import com.ssafy.intagral.ui.home.HomeFragment

private const val ARG_PARAM1 = "type" //user or hashtag
private const val ARG_PARAM2 = "data" //profile data

class ProfilePageFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null //TODO: response json -> ProfileDetail로 변경 어디서할지 생각해보기

    //post list
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private var postList = mutableListOf<PostItem>()

    private lateinit var binding: FragmentProfilePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
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

        parentFragmentManager.beginTransaction().replace(R.id.profile_detail,ProfileDetailFragment.newInstance(
            inputDataParam1, inputDataParam2)).commit()

        //TODO: API 호출
        for(i in 1..9){
            postList.add(PostItem(i, "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"))
        }

        //TODO: refactor to binding
        postRecyclerView = view.findViewById(R.id.profile_post_list)
        context?.also {
            postAdapter = PostAdapter(it, postList)
            postAdapter.onItemClickListener = object : PostAdapter.OnItemClickListener {
                override fun onClick(view: View, position: Int) {
                    Toast.makeText(it,"listener : $position", Toast.LENGTH_SHORT).show()
                }
            }
            postRecyclerView.apply {
                adapter = postAdapter
                layoutManager = GridLayoutManager(it,3)
            }
        }


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilePageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

const val inputDataParam1:Boolean = true
val inputDataParam2 = ProfileDetail(true, "yuyeon", "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png",
13,false,123,2,"dsfkdsjflkds")