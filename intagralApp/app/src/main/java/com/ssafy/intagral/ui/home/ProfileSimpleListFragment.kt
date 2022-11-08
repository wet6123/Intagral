package com.ssafy.intagral.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.ui.common.profile.ProfileSimpleAdapter

enum class ActivityType {
    MainMenuActivity,
    SearchActivity,
}

// Fragment used when searching and showing profile page
class ProfileSimpleListFragment: Fragment() {
//simple profile list
    private lateinit var profileSimpleRecyclerView: RecyclerView
    private lateinit var profileSimpleAdapter : ProfileSimpleAdapter
    private lateinit var profileSimpleList : ArrayList<ProfileSimpleItem>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_simple_list, container, false)

//simple profile list fragment
        setFragmentResultListener("search text") { requestKey, bundle ->
            bindProfileList(bundle.getString("input text"))
        }
        setFragmentResultListener("search text") { requestKey, bundle ->
            bindProfileList(bundle.getString("input text"))
        }

        profileSimpleRecyclerView = view.findViewById(R.id.search_profile_simple_list)

        return view
    }

    fun bindProfileList(inputText: String?) {
        profileSimpleList = ArrayList<ProfileSimpleItem>()
        if (inputText != "") {
            //TODO : api 호출
            profileSimpleList.add(
                ProfileSimpleItem(
                    ProfileType.user,
                    "한유연1",
                    true,
                    "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/car-967387__480.webp"
                )
            )
            profileSimpleList.add(
                ProfileSimpleItem(
                    ProfileType.hashtag,
                    "한유연2",
                    false,
                    "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/images.jfif",
                )
            )
            profileSimpleList.add(
                ProfileSimpleItem(
                    ProfileType.user,
                    "한유연3",
                    true,
                    "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/remove-background-before-qa1.png",
                )
            )

        }
        //TODO: null일 경우 처리
        context?.also {
            profileSimpleAdapter = ProfileSimpleAdapter(it, profileSimpleList)
            profileSimpleAdapter.onItemClickListener =
                object : ProfileSimpleAdapter.OnItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken,0)

                        activity?.javaClass?.simpleName?.equals(ActivityType.SearchActivity).let {
                            val activity = activity as SearchActivity
                            activity.changeActivity(1, profileSimpleList[position])
                        }

//                        val activity = activity as MainMenuActivity
//                        activity.changeFragment(1)
                        Toast.makeText(it, "listener : $position", Toast.LENGTH_SHORT).show()
                    }
                }
            profileSimpleRecyclerView.apply {
                adapter = profileSimpleAdapter
                layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            }
        }
    }


}