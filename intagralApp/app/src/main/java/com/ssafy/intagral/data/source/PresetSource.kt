package com.ssafy.intagral.data.source

import com.ssafy.intagral.data.Preset

interface PresetSource {
    fun fetchPresetData() : Preset
}