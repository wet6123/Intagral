package com.a304.intagral.api.service;

import com.a304.intagral.db.dto.HashtagProfileDto;

import java.util.List;

public interface HashtagService {
    HashtagProfileDto getProfile(Long userId, String target);

    List<String> getHotList();
}
