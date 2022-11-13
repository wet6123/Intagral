package com.ssafy.intagral.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.ui.common.profile.ProfileSimpleAdapter
import com.ssafy.intagral.viewmodel.ProfileSimpleViewModel

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

}