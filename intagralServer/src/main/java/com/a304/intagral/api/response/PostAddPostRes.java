package com.a304.intagral.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("게시글 작성 Response")
@Getter
@Setter
public class PostAddPostRes {
    @ApiModelProperty(name = "글 번호", example = "10")
    Integer postId;
}
