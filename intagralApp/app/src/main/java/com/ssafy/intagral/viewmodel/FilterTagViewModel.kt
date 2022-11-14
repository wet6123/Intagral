package com.ssafy.intagral.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.intagral.data.model.FilterTagItem
import com.ssafy.intagral.data.service.HashtagService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterTagViewModel @Inject constructor(private val hashtagService: HashtagService): ViewModel(){
    private val filterTagList: MutableLiveData<ArrayList<FilterTagItem>> = MutableLiveData()

    fun getFilterTagList(): MutableLiveData<ArrayList<FilterTagItem>> = filterTagList

    fun getHotFilterTagList() {
        viewModelScope.launch {
            filterTagList.value = hashtagService.getHotFiltertag()
        }
    }

}