package com.ssafy.intagral.ui.common.post

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(context: Context, val resource: Int, val objects: Array<PostItem>)
    : RecyclerView.Adapter<ViewHolder>() {

    //viewType 형태 아이템 뷰를 위한 뷰홀더 객체 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(resource, parent, false)

        println("onCreateViewHolder : ViewHolder 객체 생성")
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //한 행에 데이터를 넣어준다
        //holder.tvName.text = Objects[position]
        //holder.bindOnItemClickListener(onItemClickListener)

    }

    override fun getItemCount(): Int {
        return objects.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View,position:Int)
    }
    lateinit var onItemClickListener: OnItemClickListener

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener{
    //itemView에 들어갈 내용 선언 as ImageView

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        TODO("Not yet implemented")
    }

}