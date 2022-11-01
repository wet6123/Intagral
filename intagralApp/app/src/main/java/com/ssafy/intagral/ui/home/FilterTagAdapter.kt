package com.ssafy.intagral.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.R
import com.ssafy.intagral.data.FilterTagItem
import com.ssafy.intagral.data.PostItem
import com.ssafy.intagral.ui.common.post.PostViewHolder

class FilterTagAdapter(context: Context, val filterTagList: MutableList<FilterTagItem>)
    : RecyclerView.Adapter<FilterTagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterTagViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.fragment_filtertag_item, parent, false)
        return FilterTagViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FilterTagViewHolder, position: Int) {
        holder.apply {
            bindFilterTag(filterTagList[position])
            itemView.setOnClickListener{
                onItemClickListener.onClick(it,position)
            }
        }
    }

    override fun getItemCount(): Int = filterTagList.size

    interface OnItemClickListener {
        fun onClick(view: View,position:Int)
    }
    lateinit var onItemClickListener: OnItemClickListener //post list fragment 변경해야 함

}

class FilterTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindFilterTag(filterTag: FilterTagItem) {
        itemView.findViewById<AppCompatButton>(R.id.filter_tag_item).text = filterTag.tagContent
    }
}