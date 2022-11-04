package com.a304.intagral.api.service;

import com.a304.intagral.db.dto.PresetListDto;
import com.a304.intagral.db.entity.HashtagPreset;

import java.util.List;

public interface PresetService {
    List<PresetListDto> getAllPreset(Long userId, List<String> classList);

    List<String>  getAllClsName();

    PresetListDto getTargetPreset(List<HashtagPreset> hashtagPresetList, String targetCls);
}
