package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.FollowHashtagPostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowHashtagPostRes extends BaseResponseBody {
    @JsonProperty(value = "is_follow")
    boolean isFollow;
    Integer followerCnt;

    static public FollowHashtagPostRes of(int statusCode, String message, FollowHashtagPostDto followHashtagPostDto) {
        FollowHashtagPostRes res = new FollowHashtagPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setFollow(followHashtagPostDto.isFollow());
        res.setFollowerCnt(followHashtagPostDto.getFollowerCnt());

        return res;
    }
}
