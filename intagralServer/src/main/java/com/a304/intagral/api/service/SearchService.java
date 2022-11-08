package com.a304.intagral.api.service;

import com.a304.intagral.db.dto.SearchHashtagDto;
import com.a304.intagral.db.dto.SearchUserDto;

import java.util.List;

public interface SearchService {
    List<SearchUserDto> searchUser(Long userId, String target);

    List<SearchHashtagDto> searchHashtag(Long userId, String target);

    void countUpHashtagSearchCnt(String hashtag);
}
