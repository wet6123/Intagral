package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.dto.PostLikePostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLikePostRes extends BaseResponseBody {
    @JsonProperty(value = "isLike")
    boolean isLike;
    @JsonProperty(value = "likeCnt")
    Integer likeCnt;

    static public PostLikePostRes of(int statusCode, String message, PostLikePostDto postLikePostDto) {
        PostLikePostRes res = new PostLikePostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setLike(postLikePostDto.isLike());
        res.setLikeCnt(postLikePostDto.getLikeCnt());

        return res;
    }
}
