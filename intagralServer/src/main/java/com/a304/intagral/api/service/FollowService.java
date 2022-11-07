package com.a304.intagral.api.service;

import com.a304.intagral.db.dto.FollowHashtagPostDto;
import com.a304.intagral.db.dto.FollowUserPostDto;

public interface FollowService {

    FollowUserPostDto toggleUserFollow(Long userId, String nickname);

    FollowHashtagPostDto toggleHashtagFollow(Long userId, String hashtag);
}
