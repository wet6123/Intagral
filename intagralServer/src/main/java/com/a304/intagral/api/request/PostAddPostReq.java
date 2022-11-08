package com.a304.intagral.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ApiModel("게시글 작성 Request")
@Getter
@Setter
public class PostAddPostReq {
    @ApiModelProperty(name = "게시글 이미지", example = "multipartfile")
    MultipartFile image;
    @ApiModelProperty(name = "게시글 해시태그", example = "수면의자, 사무의자, 의자")
    List<String> hashtags;
}
