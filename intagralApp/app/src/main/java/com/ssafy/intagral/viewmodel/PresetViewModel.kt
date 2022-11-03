package com.ssafy.intagral.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ssafy.intagral.data.Preset
import com.ssafy.intagral.data.source.PresetRepository

class PresetViewModel {

    // preset data repository
    private val presetRepository: PresetRepository = PresetRepository()

    private var preset: MutableLiveData<Preset> = MutableLiveData()

    fun getPreset(): MutableLiveData<Preset>{
        return preset
    }

    fun addTag(targetClass: String, tag: String){
        reloadPreset()
    }

    fun removeTag(targetClass: String, tag: String){
        reloadPreset()
    }

    fun addClass(targetClass: String){
        reloadPreset()
    }

    fun removeClass(targetClass: String){
        reloadPreset()
    }

    fun reloadPreset() {
        preset.value = presetRepository.getPreset()
    }

    init {
        reloadPreset()
    }
}