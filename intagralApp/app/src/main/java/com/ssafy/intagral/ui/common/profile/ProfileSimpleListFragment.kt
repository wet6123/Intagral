package com.ssafy.intagral.ui.common.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.intagral.IntagralApplication
import com.ssafy.intagral.MainMenuActivity
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.ui.home.SearchActivity
import com.ssafy.intagral.viewmodel.ProfileSimpleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

enum class ActivityType {
    MainMenuActivity,
    SearchActivity,
}

// Fragment used when searching and showing profile page
// TODO: search activity, main menu activity 로직 통일시킬 수 있는지 생각해보기
class ProfileSimpleListFragment: Fragment() {
//simple profile list
    private lateinit var profileSimpleRecyclerView: RecyclerView
    private lateinit var profileSimpleAdapter : ProfileSimpleAdapter
    private lateinit var profileSimpleList : ArrayList<ProfileSimpleItem>

    private val profileSimpleViewModel: ProfileSimpleViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_simple_list, container, false)

        profileSimpleRecyclerView = view.findViewById(R.id.search_profile_simple_list)
        bindProfileList("")

        if(profileSimpleAdapter.itemCount == 0) {
            var isVisible: Int = View.INVISIBLE
            var infoText: String = ""
            when (profileSimpleViewModel.getProfileListPageInfo().value) {
                ProfileSimpleViewModel.ProfileListPageInfo.FOLLOWER -> {
                    isVisible = View.VISIBLE
                    infoText = "팔로워가 여기에 표시됩니다."
                }
                ProfileSimpleViewModel.ProfileListPageInfo.FOLLOWING -> {
                    isVisible = View.VISIBLE
                    infoText = "팔로잉하는 사용자가 여기에 표시됩니다."
                }
                ProfileSimpleViewModel.ProfileListPageInfo.HASHTAGFOLLOWING -> {
                    isVisible = View.VISIBLE
                    infoText = "팔로잉하는 해시태그가 여기에 표시됩니다."
                }
                else -> {
                    isVisible = View.GONE
                }
            }
            view.findViewById<ConstraintLayout>(R.id.no_follow_found_fragment).visibility = isVisible
            view.findViewById<TextView>(R.id.no_found_text).text = infoText
        }

        return view
    }

    fun bindProfileList(inputText: String?) {
        profileSimpleList = profileSimpleViewModel.getProfileSimpleList().value ?: ArrayList()

        //TODO: null일 경우 처리
        context?.also {
            profileSimpleAdapter = ProfileSimpleAdapter(it, profileSimpleList)
            profileSimpleRecyclerView.apply {
                adapter = profileSimpleAdapter
                layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            }
        }
    }

    inner class ProfileSimpleAdapter(context: Context, val profileSimpleLists: MutableList<ProfileSimpleItem>)
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
                    if(it.context.javaClass.simpleName.equals(ActivityType.SearchActivity.toString())) {
                        //TODO: search cnt++
                        val activity = it.context as SearchActivity
                        activity.changeActivity(1, profileSimpleLists[position])
                    }else if(it.context.javaClass.simpleName.equals(ActivityType.MainMenuActivity.toString())) {
                        val activity = it.context as MainMenuActivity
                        activity.changeProfileDetail(1, profileSimpleLists[position])
                    }
                }
                itemView.findViewById<Button>(R.id.profile_simple_followbtn).setOnClickListener {
                    CoroutineScope(Main).launch {
                        var toggleResult = profileSimpleViewModel.toggleFollow(profileSimpleLists[position])
                        profileSimpleLists[position] = ProfileSimpleItem(
                            profileSimpleLists[position].type,
                            profileSimpleLists[position].name,
                            toggleResult,
                            profileSimpleLists[position].imgPath,
                        )
                        notifyDataSetChanged()
                    }
                }
            }
        }

        override fun getItemCount(): Int = profileSimpleLists.size

    }

    class ProfileSimpleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bindProfileSimple(profileSimple: ProfileSimpleItem) {
            var imgPath: String = "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"

            if(profileSimple.type == ProfileType.user) {
                //TODO: check when imgPath is null
                imgPath = profileSimple.imgPath ?:"https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"
            } else {
                imgPath = "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png"
            }
            Glide.with(itemView.context).load(imgPath)
                .into(itemView.findViewById(R.id.profile_simple_img))

            var followBtn: Button = itemView.findViewById(R.id.profile_simple_followbtn)

            if(profileSimple.name.equals(IntagralApplication.prefs.nickname)){
                followBtn.visibility = View.GONE
            } else if(profileSimple.isFollow){
                followBtn.text = "Unfollow"
            }else{
                followBtn.text = "Follow"
            }

            itemView.findViewById<TextView>(R.id.profile_simple_nickname).text = profileSimple.name

        }
    }
}