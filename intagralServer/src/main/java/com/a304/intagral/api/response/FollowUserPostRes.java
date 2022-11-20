package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.FollowUserPostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "유저 팔로우 토글 Response")
@Getter
@Setter
public class FollowUserPostRes extends BaseResponseBody {
    @Schema(description = "팔로우 여부", example="true")
    @JsonProperty(value = "isFollow")
    boolean isFollow;
    @Schema(description = "해시태그 팔로워 수", example="13")
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
