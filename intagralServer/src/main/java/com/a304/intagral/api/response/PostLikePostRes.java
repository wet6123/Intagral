package com.a304.intagral.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("게시글 좋아요 Response")
@Getter
@Setter
public class PostLikePostRes {
    @ApiModelProperty(name = "좋아요 여부", example = "true")
    boolean isLike;
    @ApiModelProperty(name = "좋아요 수")
    Integer likeCnt;
}
