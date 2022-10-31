package com.ssafy.intagral.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.R
import com.ssafy.intagral.ui.common.post.PostAdapter

class FragmentHome : Fragment() {

    private lateinit var postListView: RecyclerView
    private lateinit var postListAdapter: PostAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        postListView = view.findViewById(R.id.fragment_post_list)
        postListView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.INVALID_OFFSET, false)

        postListAdapter = PostAdapter()
        //postListView.adapter = postListAdapter

        return view
    }
}