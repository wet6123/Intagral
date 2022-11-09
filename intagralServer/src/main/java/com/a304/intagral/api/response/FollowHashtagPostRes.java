package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.FollowHashtagPostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "해시태그 팔로우 토글 Response")
@Getter
@Setter
public class FollowHashtagPostRes extends BaseResponseBody {
    @Schema(description = "팔로우 여부", example="true")
    @JsonProperty(value = "isFollow")
    boolean isFollow;
    @Schema(description = "해시태그 팔로워 수", example="13")
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
