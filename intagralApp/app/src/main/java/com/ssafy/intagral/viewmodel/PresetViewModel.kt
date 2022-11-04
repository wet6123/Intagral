package com.ssafy.intagral.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.intagral.data.Preset
import com.ssafy.intagral.data.PresetClassItem
import com.ssafy.intagral.data.source.PresetRepository

class PresetViewModel: ViewModel() {

    // preset data repository
    private val presetRepository: PresetRepository = PresetRepository()

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
        val preset: Preset = presetRepository.getPreset()
        val presetClassItemList: ArrayList<PresetClassItem> = arrayListOf()
        for(className in preset.classList){
            presetClassItemList.add(
                PresetClassItem(
                    className,
                    preset.tagMap[className] ?: listOf()
                )
            )
        }

        presetList.value = presetClassItemList
    }

    init {
        reloadPresetList()
    }
}