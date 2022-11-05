package com.a304.intagral.api.service;

import com.a304.intagral.db.entity.ClassificationTarget;
import com.a304.intagral.db.entity.HashtagPreset;
import com.a304.intagral.db.repository.ClassificationTargetRepository;
import com.a304.intagral.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("presetService")
public class PresetServiceImpl implements PresetService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClassificationTargetRepository classificationTargetRepository;

    @Override
    public Map<String, List<String>> getAllPreset(Long userId, List<String> classList) {
        List<HashtagPreset> hashtagPresetList = userRepository.findById(userId).get().getHashtagPresetList();
        Map<String, List<String>> presetList = new HashMap<>();
        for (String cls : classList) {
            presetList.put(cls, getTargetPreset(hashtagPresetList, cls));
        }
        return presetList;
    }

    @Override
    public List<String> getAllClsName() {
        List<ClassificationTarget> allClsTarget = classificationTargetRepository.findAll();

        List<String> list = new ArrayList<>();
        for(ClassificationTarget classificationTarget : allClsTarget){
            list.add(classificationTarget.getTargetName());
        }

        return list;
    }

    @Override
    public List<String> getTargetPreset(List<HashtagPreset> hashtagPresetList, String targetCls) {
        List<String> tagList = new ArrayList<>();
        for(HashtagPreset hashtagPreset : hashtagPresetList){
            if(targetCls.equals(hashtagPreset.getHashtagPresetToClassificationTarget().getTargetName())){
                tagList.add(hashtagPreset.getHashtagPresetToHashtag().getContent());
            }
        }

        return tagList;
    }
}
