package com.ssafy.intagral.ui.common.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.intagral.R
import com.ssafy.intagral.data.ProfileSimpleItem

class ProfileSimpleAdapter(context: Context, val profileSimpleLists: MutableList<ProfileSimpleItem>)
    :RecyclerView.Adapter<ProfileSimpleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileSimpleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.fragment_profile_simple, parent, false)
        return ProfileSimpleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfileSimpleViewHolder, position: Int) {
        holder.apply {
            bindProfileSimple(profileSimpleLists[position])
            itemView.setOnClickListener{
                onItemClickListener.onClick(it,position)
            }
        }
    }

    override fun getItemCount(): Int = profileSimpleLists.size

    interface OnItemClickListener {
        fun onClick(view: View,position:Int)
    }
    lateinit var onItemClickListener: OnItemClickListener //profile page로 이동
}

class ProfileSimpleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    fun bindProfileSimple(profileSimple: ProfileSimpleItem) {
        val imgPath: String
        val followBtnText: String
        if(profileSimple.profileSimpleType.equals("user")) {
            imgPath = profileSimple.profileSimpleImgPath
        } else {
            imgPath = "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"
        }

        if(profileSimple.profileSimpleIsFollow){
            followBtnText = "Unfollow"
        }else{
            followBtnText = "Follow"
        }
        Glide.with(itemView.context).load(imgPath)
            .into(itemView.findViewById(R.id.profile_simple_img))
        itemView.findViewById<TextView>(R.id.profile_simple_nickname).text = profileSimple.profileSimpleNickName
        itemView.findViewById<Button>(R.id.profile_simple_followbtn).text = followBtnText
    }
}