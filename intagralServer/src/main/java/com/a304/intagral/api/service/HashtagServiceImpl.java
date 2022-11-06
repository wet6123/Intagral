package com.a304.intagral.api.service;

import com.a304.intagral.db.dto.HashtagProfileDto;
import com.a304.intagral.db.entity.Hashtag;
import com.a304.intagral.db.repository.HashtagFollowRepository;
import com.a304.intagral.db.repository.HashtagRepository;
import com.a304.intagral.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("hashtagService")
public class HashtagServiceImpl implements HashtagService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    HashtagRepository hashtagRepository;
    @Autowired
    HashtagFollowRepository hashtagFollowRepository;

    @Override
    public HashtagProfileDto getProfile(Long userId, String target) {

        Hashtag hashtag = hashtagRepository.findByContent(target).get();
        Integer tagId = hashtag.getId().intValue();

        Long followerCnt = hashtagFollowRepository.countByHashtagId(tagId);
        Long isFollow = hashtagFollowRepository.countByUserIdAndHashtagId(userId.intValue(), tagId);

        HashtagProfileDto hashtagProfileDto = HashtagProfileDto.builder()
                .content(target)
                .followerCnt(followerCnt.intValue())
                .isFollow(isFollow != 0)
                .build();

        return hashtagProfileDto;
    }

    @Override
    public List<String> getHotList() {
        List<Hashtag> hashtagList = hashtagRepository.findTop5ByOrderBySearchCntDesc();
        List<String> hotList = new ArrayList<>();
        for (Hashtag hashtag : hashtagList) {
            hotList.add(hashtag.getContent());
        }

        return hotList;
    }

}
