package com.ssafy.intagral.ui.common.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.R
import com.ssafy.intagral.data.PostItem

class PostListFragment :Fragment() {
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private var postList = mutableListOf<PostItem>()

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}