package com.ssafy.intagral.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.intagral.data.PresetClassItem
import com.ssafy.intagral.data.source.preset.PresetRepository
import com.ssafy.intagral.data.source.preset.PresetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PresetViewModel @Inject constructor(private val repository: PresetRepository): ViewModel() {

    private var presetList: MutableLiveData<ArrayList<PresetClassItem>> = MutableLiveData()

    fun getPresetList(): MutableLiveData<ArrayList<PresetClassItem>>{
        return presetList
    }

    fun addClass(targetClass: String){
        reloadPresetList()
    }

    fun removeClass(targetClass: String){
        reloadPresetList()
    }

    fun addTag(targetClass: String, tag: String){
        reloadPresetList()
    }

    fun removeTag(targetClass: String, tag: String){
        reloadPresetList()
    }

    private fun reloadPresetList() {
        viewModelScope.launch{
            val result: ArrayList<PresetClassItem> = arrayListOf()

            repository.fetchPresetItemList().enqueue(object : Callback<PresetResponse> {
                override fun onResponse(
                    call: Call<PresetResponse>,
                    response: Response<PresetResponse>
                ) {
                    if(response.isSuccessful){
                        response.body()?.let {
                            for(className in it.classList){
                                result.add(PresetClassItem(
                                    className,
                                    it.data[className]!!
                                ))
                            }
                            presetList.value = result
                        }
                    }else{
                        Log.d("RETROFIT", "응답 에러 : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<PresetResponse>, t: Throwable) {
                    Log.d("RETROFIT", "onFailure 에러: " + t.message.toString());
                }
            })
        }
    }

    init {
        reloadPresetList()
    }
}