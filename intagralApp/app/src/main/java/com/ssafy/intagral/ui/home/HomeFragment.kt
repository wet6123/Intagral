package com.ssafy.intagral.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.FilterTagItem
import com.ssafy.intagral.data.model.FilterType
import com.ssafy.intagral.data.model.PostItem
import com.ssafy.intagral.ui.common.post.PostListFragment
import com.ssafy.intagral.viewmodel.FilterTagViewModel
import com.ssafy.intagral.viewmodel.PostListViewModel

class HomeFragment : Fragment() {

    //filter hashtag list
    private lateinit var filterTagRecyclerView: RecyclerView
    private lateinit var filterTagAdapter: FilterTagAdapter
    private var filterTagList: ArrayList<FilterTagItem> = ArrayList()

    private val postListViewModel: PostListViewModel by activityViewModels()
    private val filterTagViewModel: FilterTagViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        
        //TODO: tag선택 따라서 PostListViewModel 변경
        postListViewModel.initPage("all", 1, null)
        parentFragmentManager.beginTransaction().replace(R.id.fragment_post_list, PostListFragment()).commit()

//filter tag fragment
        filterTagRecyclerView = view.findViewById(R.id.fragment_filter_tag_list)
        filterTagViewModel.getHotFilterTagList()
        filterTagViewModel.getFilterTagList().observe(viewLifecycleOwner){
            filterTagList = filterTagViewModel.getFilterTagList().value!!

            context?.also {
                filterTagAdapter = FilterTagAdapter(it, filterTagList)
                filterTagAdapter.onItemClickListener = object: FilterTagAdapter.OnItemClickListener {
                    override fun onClick(view:View, position: Int) {
                        postListViewModel.initPage(filterTagList[position].type.toString(), 1, filterTagList[position].tagContent)
                    }
                }
                filterTagRecyclerView.apply {
                    adapter = filterTagAdapter
                    layoutManager = LinearLayoutManager(it, RecyclerView.HORIZONTAL, false)
                }
            }

        }

        postListViewModel.getPostList().observe(viewLifecycleOwner) {
            parentFragmentManager.beginTransaction().replace(R.id.fragment_post_list, PostListFragment()).commit()
        }

        return view
    }
}