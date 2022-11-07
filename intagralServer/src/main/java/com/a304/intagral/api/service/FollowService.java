package com.a304.intagral.api.service;

import com.a304.intagral.db.dto.*;

import java.util.List;

public interface FollowService {

    FollowUserPostDto toggleUserFollow(Long userId, String nickname);

    FollowHashtagPostDto toggleHashtagFollow(Long userId, String hashtag);

    List<FollowUserGetDto> getUserFollowingList(Long userId, String nickname);

    List<FollowUserGetDto> getFollowerList(Long userId, String nickname);

    List<FollowingHashtagGetDto> getHashtagFollowingList(Long userId, String nickname);

    List<FollowUserGetDto> getHashtagFollowerList(Long userId, String hashtag);
}
