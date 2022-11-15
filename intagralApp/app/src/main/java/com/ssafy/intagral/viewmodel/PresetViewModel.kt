package com.ssafy.intagral.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.ssafy.intagral.data.model.PresetClassItem
import com.ssafy.intagral.data.service.PresetService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PresetViewModel @Inject constructor(private val service: PresetService): ViewModel() {

    private var presetList: MutableLiveData<ArrayList<PresetClassItem>> = MutableLiveData()

    fun getPresetList(): MutableLiveData<ArrayList<PresetClassItem>>{
        return presetList
    }

    fun addTag(targetClass: String, tag: String){
        viewModelScope.launch {
            val json = JsonObject()
            json.addProperty("cls", targetClass)
            json.addProperty("data", tag)
            val response = service.addPresetTag(json)
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
            val response = service.deletePresetTag(json)
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
            val response = service.fetchPresetItemList()
            if(response.isSuccessful){
                response.body()?.let {
                    for(className in it.classList){
                        result.add(
                            PresetClassItem(
                            className,
                            it.classListKor[className]!!,
                            it.data[className]!!
                        )
                        )
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