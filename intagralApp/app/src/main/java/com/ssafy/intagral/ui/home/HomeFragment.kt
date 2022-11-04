package com.ssafy.intagral.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar.OnMenuItemClickListener
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.MainMenuActivity
import com.ssafy.intagral.R
import com.ssafy.intagral.data.FilterTagItem
import com.ssafy.intagral.data.PostItem
import com.ssafy.intagral.ui.common.post.PostAdapter

class HomeFragment : Fragment() {

    //post list
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private var postList = mutableListOf<PostItem>()
    //filter hashtag list
    private lateinit var filterTagRecyclerView: RecyclerView
    private lateinit var filterTagAdapter: FilterTagAdapter
    private var filterTagList = mutableListOf<FilterTagItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

//post list fragment
        postRecyclerView = view.findViewById(R.id.fragment_post_list)

        //temporary items
        //TODO: make newInstance method to call appropriate API
        for(i in 1..50){
            postList.add(PostItem(i, "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"))
        }
        //TODO: null일 경우 추가
        context?.also {
            postAdapter = PostAdapter(it, postList)
            postAdapter.onItemClickListener = object : PostAdapter.OnItemClickListener {
                override fun onClick(view: View, position: Int) {
                    //TODO: API 호출
                    Toast.makeText(it,"listener : $position",Toast.LENGTH_SHORT).show()
                }
            }
            postRecyclerView.apply {
                adapter = postAdapter
                layoutManager = GridLayoutManager(it,3)
            }
        }

//filter tag fragment
        filterTagRecyclerView = view.findViewById(R.id.fragment_filter_tag_list)
        //temporary items
        filterTagList.add(FilterTagItem(1,"전체"))
        filterTagList.add(FilterTagItem(2,"팔로우"))

        //TODO: null일 경우 추가
        context?.also {
            filterTagAdapter = FilterTagAdapter(it, filterTagList)
            filterTagAdapter.onItemClickListener = object: FilterTagAdapter.OnItemClickListener {
                override fun onClick(view:View, position: Int) {
                    //TODO: API 호출
                    Toast.makeText(it, "listener: $position", Toast.LENGTH_SHORT).show()
                }
            }
            filterTagRecyclerView.apply {
                adapter = filterTagAdapter
                layoutManager = LinearLayoutManager(it, RecyclerView.HORIZONTAL, false)
            }
        }


        return view
    }
}