package com.a304.intagral.api.service;

import com.a304.intagral.db.dto.HashtagProfileDto;

public interface HashtagService {
    HashtagProfileDto getProfile(Long userId, String target);
}
