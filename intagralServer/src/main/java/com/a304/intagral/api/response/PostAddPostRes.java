package com.a304.intagral.api.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "게시글 작성 Response")
@Getter
@Setter
public class PostAddPostRes {
    @Schema(name = "글 번호", example = "10")
    Integer postId;
}
