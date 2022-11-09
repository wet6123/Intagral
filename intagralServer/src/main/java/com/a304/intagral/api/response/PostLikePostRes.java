package com.a304.intagral.api.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "게시글 좋아요 Response")
@Getter
@Setter
public class PostLikePostRes {
    @Schema(name = "좋아요 여부", example = "true")
    boolean isLike;
    @Schema(name = "좋아요 수")
    Integer likeCnt;
}
