package com.a304.intagral.api.response;


import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.dto.PostLikePostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "게시글 좋아요 Response")
@Getter
@Setter
public class PostLikePostRes extends BaseResponseBody {
    @Schema(name = "좋아요 여부", example = "true")
    @JsonProperty(value = "isLike")
    boolean isLike;
    @Schema(name = "좋아요 수")
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
