package com.ssafy.intagral.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.ui.common.profile.ProfileSimpleAdapter
import com.ssafy.intagral.viewmodel.ProfileSimpleViewModel

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

    private val profileSimpleViewModel: ProfileSimpleViewModel by activityViewModels()

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
        profileSimpleList = profileSimpleViewModel.getProfileSimpleList().value ?: ArrayList()

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
                        //TODO: Following list 페이지, main menu activity에서 호출
                    }
                }
            profileSimpleRecyclerView.apply {
                adapter = profileSimpleAdapter
                layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            }
        }
    }


}