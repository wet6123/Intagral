package com.a304.intagral.api.service;

import com.a304.intagral.db.dto.FollowUserPostDto;
import com.a304.intagral.db.entity.User;
import com.a304.intagral.db.entity.UserFollow;
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
}
