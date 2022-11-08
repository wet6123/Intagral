package com.ssafy.intagral.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.ssafy.intagral.data.PresetClassItem
import com.ssafy.intagral.data.source.preset.PresetRepository
import com.ssafy.intagral.data.source.preset.PresetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
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

    fun addTag(targetClass: String, tag: String){
        viewModelScope.launch {
            val json = JsonObject()
            json.addProperty("cls", targetClass)
            json.addProperty("data", tag)
            val response = repository.addPresetTag(json)
            if(response.isSuccessful){
                reloadPresetList()
            }else{
                Log.d("RETROFIT /api/preset/add", "응답 에러 : ${response.code()}")
            }
        }
    }

    fun removeTag(targetClass: String, tag: String){
        viewModelScope.launch {
            val json = JsonObject()
            json.addProperty("cls", targetClass)
            json.addProperty("data", tag)
            val response = repository.deletePresetTag(json)
            if(response.isSuccessful){
                reloadPresetList()
            }else{
                Log.d("RETROFIT /api/preset/delete", "응답 에러 : ${response.code()}")
            }
        }
    }

    private fun reloadPresetList() {
        viewModelScope.launch{
            val result: ArrayList<PresetClassItem> = arrayListOf()
            val response = repository.fetchPresetItemList()
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
                Log.d("RETROFIT /api/preset/list", "응답 에러 : ${response.code()}")
            }
        }
    }

    init {
        reloadPresetList()
    }
}