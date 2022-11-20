package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.HashtagProfileDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "해시태그 프로필 정보")
@Getter
@Setter
public class HashtagProfileRes extends BaseResponseBody {
    @Schema(name = "대상", example = "[user | hashtag]")
    String type;
    @Schema(name = "태그명", example = "허먼밀러")
    String content;
    @Schema(name = "팔로워수", example = "54")
    Integer follower;
    @Schema(name = "팔로우여부", example = "false")
    @JsonProperty(value = "isFollow")
    boolean isFollow;

    public static HashtagProfileRes of(int statusCode, String message, HashtagProfileDto hashtagProfileDto) {
        HashtagProfileRes res = new HashtagProfileRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setType("hashtag");
        res.setContent(hashtagProfileDto.getContent());
        res.setFollower(hashtagProfileDto.getFollowerCnt());
        res.setFollow(hashtagProfileDto.isFollow());

        return res;
    }
}
