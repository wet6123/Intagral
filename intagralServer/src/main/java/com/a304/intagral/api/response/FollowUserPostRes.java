package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.FollowUserPostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowUserPostRes extends BaseResponseBody {
    @JsonProperty(value = "is_follow")
    boolean isFollow;
    Integer followerCnt;

    static public FollowUserPostRes of(int statusCode, String message, FollowUserPostDto followUserPostDto) {
        FollowUserPostRes res = new FollowUserPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setFollow(followUserPostDto.isFollow());
        res.setFollowerCnt(followUserPostDto.getFollowerCnt());

        return res;
    }
}
