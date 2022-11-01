package com.ssafy.intagral.ui.common.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.intagral.R
import com.ssafy.intagral.data.PostItem

class PostAdapter(context: Context, val postLists: MutableList<PostItem>)
    : RecyclerView.Adapter<PostViewHolder>() {

    //viewType 형태 아이템 뷰를 위한 뷰홀더 객체 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.fragment_postitem, parent, false)

        println("onCreateViewHolder : ViewHolder 객체 생성")
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        //한 행에 데이터를 넣어준다
        holder.apply {
            bindPost(postLists[position])
            itemView.setOnClickListener{
                onItemClickListener.onClick(it,position)
            }
        }

    }

    override fun getItemCount(): Int {
        return postLists.size
    }

    //외부에서 OnItemClickListener 공급받기 위한 작업
    interface OnItemClickListener {
        fun onClick(view: View,position:Int)
    }
    lateinit var onItemClickListener: OnItemClickListener //detail로 연결

}

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //itemView에 들어갈 내용 선언 as ImageView
    fun bindPost(post: PostItem) {
        //itemView.findViewById<ImageView>(R.id.dummyPostItem).id = post.postId //??
        Glide.with(itemView.context).load(post.imgPath).into(itemView.findViewById(R.id.dummyPostItem)) //R.id.~
    }
}