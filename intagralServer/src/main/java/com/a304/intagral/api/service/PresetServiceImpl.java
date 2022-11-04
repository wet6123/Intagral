package com.a304.intagral.api.service;

import com.a304.intagral.db.dto.PresetListDto;
import com.a304.intagral.db.entity.ClassificationTarget;
import com.a304.intagral.db.entity.HashtagPreset;
import com.a304.intagral.db.repository.ClassificationTargetRepository;
import com.a304.intagral.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("presetService")
public class PresetServiceImpl implements PresetService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClassificationTargetRepository classificationTargetRepository;

    @Override
    public List<PresetListDto> getAllPreset(Long userId, List<String> classList) {
        List<HashtagPreset> hashtagPresetList = userRepository.findById(userId).get().getHashtagPresetList();
        List<PresetListDto> presetListDtoList = new ArrayList<>();
        for (String cls : classList) {
            presetListDtoList.add(getTargetPreset(hashtagPresetList, cls));
        }
        return presetListDtoList;
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
    public PresetListDto getTargetPreset(List<HashtagPreset> hashtagPresetList, String targetCls) {
        List<String> tagList = new ArrayList<>();
        for(HashtagPreset hashtagPreset : hashtagPresetList){
            if(targetCls.equals(hashtagPreset.getHashtagPresetToClassificationTarget().getTargetName())){
                tagList.add(hashtagPreset.getHashtagPresetToHashtag().getContent());
            }
        }

        return new PresetListDto(targetCls, tagList);
    }
}
