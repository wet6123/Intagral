package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.UserProfileDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRes extends BaseResponseBody {
    String type;
    String nickname;
    String intro;
    Long following;
    Long follower;
    Long hashtag;
    @JsonProperty(value = "is_follow")
    boolean isFollow;

    static public UserProfileRes of(Integer statusCode, String message, UserProfileDto userProfileDto) {
        UserProfileRes res = new UserProfileRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setType("user");
        res.setNickname(userProfileDto.getNickname());
        res.setIntro(userProfileDto.getIntro());
        res.setFollowing(userProfileDto.getFollowing());
        res.setFollower(userProfileDto.getFollower());
        res.setHashtag(userProfileDto.getHashtag());
        res.setFollow(userProfileDto.isFollow());

        return res;
    }
}
