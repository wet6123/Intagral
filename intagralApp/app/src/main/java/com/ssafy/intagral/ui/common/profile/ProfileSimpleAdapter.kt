package com.ssafy.intagral.ui.common.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.intagral.MainMenuActivity
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.ui.home.SearchActivity

enum class ActivityType {
    MainMenuActivity,
    SearchActivity,
}

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
            itemView.findViewById<LinearLayout>(R.id.profile_simple_imgAndName).setOnClickListener{
//                val imm = it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(it.windowToken, 0)
                if(it.context.javaClass.simpleName.equals(ActivityType.SearchActivity.toString())) {
                    val activity = it.context as SearchActivity
                    activity.changeActivity(1, profileSimpleLists[position])
                }else if(it.context.javaClass.simpleName.equals(ActivityType.MainMenuActivity.toString())) {
                    val activity = it.context as MainMenuActivity
                    activity.changeProfileDetail(1, profileSimpleLists[position])
                }else{

                }
            }
            itemView.findViewById<Button>(R.id.profile_simple_followbtn).setOnClickListener {
                println("follow 요청 보냄")
            }
        }
    }

    override fun getItemCount(): Int = profileSimpleLists.size

    interface OnItemClickListener {
        fun onClick(view: View,position:Int)
    }
//    lateinit var onItemClickListener: OnItemClickListener // TODO: profile page로 이동
}

class ProfileSimpleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    fun bindProfileSimple(profileSimple: ProfileSimpleItem) {
        val imgPath: String
        val followBtnText: String
        if(profileSimple.type == ProfileType.user) {
            //TODO: check when imgPath is null
            imgPath = profileSimple.imgPath ?:"https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"
        } else {
            imgPath = "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"
        }

        if(profileSimple.isFollow){
            followBtnText = "Unfollow"
        }else{
            followBtnText = "Follow"
        }
        Glide.with(itemView.context).load(imgPath)
            .into(itemView.findViewById(R.id.profile_simple_img))
        itemView.findViewById<TextView>(R.id.profile_simple_nickname).text = profileSimple.name
        itemView.findViewById<Button>(R.id.profile_simple_followbtn).text = followBtnText

    }
}