package com.a304.intagral.api.service;

import com.a304.intagral.api.request.PresetAddPostReq;
import com.a304.intagral.db.entity.HashtagPreset;

import java.util.List;
import java.util.Map;

public interface PresetService {
    Map<String, List<String>> getAllPreset(Long userId, List<String> classList);

    List<String> getAllClsName();

    List<String> getTargetPreset(List<HashtagPreset> hashtagPresetList, String targetCls);

    void addPreset(Long userId, PresetAddPostReq presetAddPostReq);
}
