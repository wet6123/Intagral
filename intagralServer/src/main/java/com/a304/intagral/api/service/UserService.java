package com.a304.intagral.api.service;

import com.a304.intagral.api.response.TokenRes;
import com.a304.intagral.db.dto.UserProfileDto;
import com.a304.intagral.db.entity.User;

public interface UserService {
    public User getUserByUserId(Long userId);

    public User getUserByUserName(String nickcname);

    public TokenRes login(String idToken);

    public void logout(Long userId);

    UserProfileDto getProfile(Long userId, String nickname);
}
