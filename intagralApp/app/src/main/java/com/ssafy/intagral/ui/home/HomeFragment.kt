package com.ssafy.intagral.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.R
import com.ssafy.intagral.data.PostItem
import com.ssafy.intagral.ui.common.post.PostAdapter

class HomeFragment : Fragment() {

    private lateinit var postRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private var postList = mutableListOf<PostItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        postRecyclerView = view.findViewById(R.id.fragment_post_list)

        //temporary items
        for(i in 1..9){
            postList.add(PostItem(i, "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"))
        }
        context?.also {
            postAdapter = PostAdapter(it, postList)
            postAdapter.onItemClickListener = object : PostAdapter.OnItemClickListener {
                override fun onClick(view: View, position: Int) {
                    Toast.makeText(it,"listener : $position",Toast.LENGTH_SHORT).show()
                }
            }
            postRecyclerView.apply {
                adapter = postAdapter
                layoutManager = GridLayoutManager(it,3)
            }
        }

        return view
    }
}