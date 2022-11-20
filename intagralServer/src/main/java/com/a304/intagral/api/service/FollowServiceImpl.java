package com.a304.intagral.api.service;

import com.a304.intagral.db.dto.*;
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

import java.util.ArrayList;
import java.util.List;

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

        boolean isFollow = userFollowRepository.countByUserIdToAndUserIdFrom(targetUserId, myUserId) != 0;

        if(isFollow){
            userFollowRepository.deleteByUserIdToAndUserIdFrom(targetUserId, myUserId);
        } else {
            UserFollow userFollow = UserFollow.builder()
                    .userIdTo(targetUserId)
                    .userIdFrom(myUserId)
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

    // TODO: N + 1
    @Override
    public List<FollowUserGetDto> getUserFollowingList(Long userId, String nickname) {
        User targetUser = userRepository.findByNickname(nickname).get();

        List<UserFollow> userFollowingList = targetUser.getUserFollowingList();
        List<Long> userIdList = new ArrayList<>();
        for (UserFollow userFollow : userFollowingList) {
            userIdList.add(userFollow.getUserIdTo().longValue());
        }
        List<User> userList = userRepository.findByIdIn(userIdList);

        List<FollowUserGetDto> followList = new ArrayList<>();
        for (User user : userList) {
            boolean isFollow = userFollowRepository.countByUserIdToAndUserIdFrom(user.getId().intValue(), userId.intValue()) != 0;
            FollowUserGetDto followUserGetDto = FollowUserGetDto.builder()
                    .imagePath(user.getProfileImgPath())
                    .nickname(user.getNickname())
                    .intro(user.getIntro())
                    .followerCnt(user.getUserFollowerList().size())
                    .isFollow(isFollow)
                    .build();

            followList.add(followUserGetDto);
        }

        return followList;
    }

    @Override
    public List<FollowUserGetDto> getFollowerList(Long userId, String nickname) {
        User targetUser = userRepository.findByNickname(nickname).get();

        List<UserFollow> userFollowerList = targetUser.getUserFollowerList();
        List<Long> userIdList = new ArrayList<>();
        for (UserFollow userFollow : userFollowerList) {
            userIdList.add(userFollow.getUserIdFrom().longValue());
        }
        List<User> userList = userRepository.findByIdIn(userIdList);

        List<FollowUserGetDto> followList = new ArrayList<>();
        for (User user : userList) {
            boolean isFollow = userFollowRepository.countByUserIdToAndUserIdFrom(user.getId().intValue(), userId.intValue()) != 0;
            FollowUserGetDto followUserGetDto = FollowUserGetDto.builder()
                    .imagePath(user.getProfileImgPath())
                    .nickname(user.getNickname())
                    .intro(user.getIntro())
                    .followerCnt(user.getUserFollowerList().size())
                    .isFollow(isFollow)
                    .build();

            followList.add(followUserGetDto);
        }

        return followList;
    }

    @Override
    public List<FollowingHashtagGetDto> getHashtagFollowingList(Long userId, String nickname) {
        User targetUser = userRepository.findByNickname(nickname).get();

        List<HashtagFollow> hashtagFollowList = targetUser.getHashtagFollowList();
        List<Long> hashtagIdList = new ArrayList<>();
        for (HashtagFollow hashtagFollow : hashtagFollowList) {
            hashtagIdList.add(hashtagFollow.getHashtagId().longValue());
        }
        List<Hashtag> hashtagList = hashtagRepository.findByIdIn(hashtagIdList);

        List<FollowingHashtagGetDto> followList = new ArrayList<>();
        for (Hashtag hashtag : hashtagList) {
            boolean isFollow = hashtagFollowRepository.countByUserIdAndHashtagId(userId.intValue(), hashtag.getId().intValue()) != 0;
            FollowingHashtagGetDto followingHashtagGetDto = FollowingHashtagGetDto.builder()
                    .nickname(hashtag.getContent())
                    .followerCnt(hashtag.getHashtagFollowList().size())
                    .isFollow(isFollow)
                    .build();

            followList.add(followingHashtagGetDto);
        }

        return followList;
    }

    @Override
    public List<FollowUserGetDto> getHashtagFollowerList(Long userId, String hashtag) {
        Hashtag targetHashtag = hashtagRepository.findByContent(hashtag).get();

        List<HashtagFollow> hashtagFollowList = targetHashtag.getHashtagFollowList();
        List<Long> userIdList = new ArrayList<>();
        for (HashtagFollow hashtagFollow : hashtagFollowList) {
            userIdList.add(hashtagFollow.getUserId().longValue());
        }
        List<User> userList = userRepository.findByIdIn(userIdList);

        List<FollowUserGetDto> followList = new ArrayList<>();
        for (User user : userList) {
            boolean isFollow = userFollowRepository.countByUserIdToAndUserIdFrom(user.getId().intValue(), userId.intValue()) != 0;
            FollowUserGetDto followUserGetDto = FollowUserGetDto.builder()
                    .imagePath(user.getProfileImgPath())
                    .nickname(user.getNickname())
                    .intro(user.getIntro())
                    .followerCnt(user.getUserFollowerList().size())
                    .isFollow(isFollow)
                    .build();

            followList.add(followUserGetDto);
        }

        return followList;
    }
}
