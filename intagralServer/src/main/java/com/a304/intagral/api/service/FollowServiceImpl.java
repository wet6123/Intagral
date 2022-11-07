package com.a304.intagral.api.service;

import com.a304.intagral.db.dto.FollowHashtagPostDto;
import com.a304.intagral.db.dto.FollowUserPostDto;
import com.a304.intagral.db.entity.Hashtag;
import com.a304.intagral.db.entity.HashtagFollow;
import com.a304.intagral.db.entity.User;
import com.a304.intagral.db.entity.UserFollow;
import com.a304.intagral.db.repository.HashtagFollowRepository;
import com.a304.intagral.db.repository.HashtagRepository;
import com.a304.intagral.db.repository.UserFollowRepository;
import com.a304.intagral.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("followService")
public class FollowServiceImpl implements FollowService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserFollowRepository userFollowRepository;
    @Autowired
    HashtagRepository hashtagRepository;
    @Autowired
    HashtagFollowRepository hashtagFollowRepository;

    @Override
    public FollowUserPostDto toggleUserFollow(Long userId, String nickname) {

        User targetUser = userRepository.findByNickname(nickname).get();
        Integer myUserId = userId.intValue();
        Integer targetUserId = targetUser.getId().intValue();

        boolean isFollow = userFollowRepository.countByUserIdToAndUserIdFrom(myUserId, targetUserId) != 0;

        if(isFollow){
            userFollowRepository.deleteByUserIdToAndUserIdFrom(myUserId, targetUserId);
        } else {
            UserFollow userFollow = UserFollow.builder()
                    .userIdTo(myUserId)
                    .userIdFrom(targetUserId)
                    .build();
            userFollowRepository.save(userFollow);
        }

        FollowUserPostDto followUserPostDto = FollowUserPostDto.builder()
                .isFollow(!isFollow)
                .followerCnt(targetUser.getUserFollowerList().size())
                .build();

        return followUserPostDto;
    }

    @Override
    public FollowHashtagPostDto toggleHashtagFollow(Long userId, String hashtag) {
        Hashtag targetHashtag = hashtagRepository.findByContent(hashtag).get();
        Integer myUserId = userId.intValue();
        Integer targetHashtagId = targetHashtag.getId().intValue();

        boolean isFollow = hashtagFollowRepository.countByUserIdAndHashtagId(myUserId, targetHashtagId) != 0;

        if(isFollow){
            hashtagFollowRepository.deleteByUserIdAndHashtagId(myUserId, targetHashtagId);
        } else {
            HashtagFollow hashtagFollow = HashtagFollow.builder()
                    .userId(myUserId)
                    .hashtagId(targetHashtagId)
                    .build();
            hashtagFollowRepository.save(hashtagFollow);
        }

        FollowHashtagPostDto followHashtagPostDto = FollowHashtagPostDto.builder()
                .isFollow(!isFollow)
                .followerCnt(targetHashtag.getHashtagFollowList().size())
                .build();

        return followHashtagPostDto;
    }
}
