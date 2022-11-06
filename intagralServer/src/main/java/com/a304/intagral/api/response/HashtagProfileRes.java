package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.HashtagProfileDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HashtagProfileRes extends BaseResponseBody {
    String type;
    String content;
    Integer follower;
    @JsonProperty(value = "is_follow")
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
