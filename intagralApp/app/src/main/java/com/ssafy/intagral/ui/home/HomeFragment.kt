package com.ssafy.intagral.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.FilterTagItem
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
                filterTagRecyclerView.apply {
                    adapter = filterTagAdapter
                    layoutManager = LinearLayoutManager(it, RecyclerView.HORIZONTAL, false)
                }
            }

        }

        return view
    }


    inner class FilterTagAdapter(context: Context, val filterTagList: MutableList<FilterTagItem>)
        : RecyclerView.Adapter<FilterTagViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterTagViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.view_filtertag_item, parent, false)
            return FilterTagViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: FilterTagViewHolder, position: Int) {
            holder.apply {
                bindFilterTag(filterTagList[position])
                itemView.setOnClickListener{
                    filterTagViewModel.selectFilterTag(position)
                    it.isSelected = true


                    postListViewModel.initPage(filterTagList[position].type.toString(), 1, filterTagList[position].tagContent)
                    parentFragmentManager.beginTransaction().replace(R.id.fragment_post_list, PostListFragment()).commit()
                }

                filterTagViewModel.getFilterTagSelected().observe(viewLifecycleOwner) {
                    if(this.adapterPosition != it) {
                        this.itemView.isSelected = false
                    }
                }
            }
        }

        override fun getItemCount(): Int = filterTagList.size

        override fun onViewAttachedToWindow(holder: FilterTagViewHolder) {
            super.onViewAttachedToWindow(holder)
            holder.itemView.isSelected = holder.adapterPosition == filterTagViewModel.getFilterTagSelected().value
        }

    }

    inner class FilterTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindFilterTag(filterTag: FilterTagItem) {
            itemView.findViewById<AppCompatButton>(R.id.filter_tag_item).text = filterTag.tagContent
        }
    }
}