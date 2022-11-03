package com.ssafy.intagral.data.source

import com.ssafy.intagral.data.Preset

class PresetRepository {
    private val presetSource : PresetSource = PresetSourceLocalImpl()

    fun getPreset() : Preset{
        return presetSource.fetchPresetData()

    }
}