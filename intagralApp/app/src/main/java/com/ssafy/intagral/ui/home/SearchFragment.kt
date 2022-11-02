package com.ssafy.intagral.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.R
import com.ssafy.intagral.data.ProfileSimpleItem
import com.ssafy.intagral.ui.common.profile.ProfileSimpleAdapter

class SearchFragment: Fragment() {
//simple profile list
    private lateinit var profileSimpleRecyclerView: RecyclerView
    private lateinit var profileSimpleAdapter : ProfileSimpleAdapter
    private var profileSimpleList = mutableListOf<ProfileSimpleItem>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

//simple profile list fragment
//TODO: binding simple profiles when typing text into searchbar
//TODO: pop elements from profileSimpleList when click event (on simple profile) occurs
        profileSimpleRecyclerView = view.findViewById(R.id.search_profile_simple_list)

        profileSimpleList.add(ProfileSimpleItem(1, "user", "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/car-967387__480.webp", "한유연1", true))
        profileSimpleList.add(ProfileSimpleItem(2, "hashtag", "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/images.jfif", "한유연2", false))
        profileSimpleList.add(ProfileSimpleItem(3, "user", "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/remove-background-before-qa1.png", "한유연3", true))

        context?.also {
            profileSimpleAdapter = ProfileSimpleAdapter(it, profileSimpleList)
            profileSimpleAdapter.onItemClickListener = object : ProfileSimpleAdapter.OnItemClickListener {
                override fun onClick(view: View, position: Int) {
                    Toast.makeText(it, "listener : $position", Toast.LENGTH_SHORT).show()
                }
            }
            profileSimpleRecyclerView.apply {
                adapter = profileSimpleAdapter
                layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            }
        }
        return view
    }
}