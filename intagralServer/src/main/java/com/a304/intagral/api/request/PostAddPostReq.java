package com.a304.intagral.api.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema(description = "게시글 작성 Request")
@Getter
@Setter
public class PostAddPostReq {
    @Schema(name = "게시글 이미지", example = "multipartfile")
    MultipartFile image;
    @Schema(name = "게시글 해시태그", example = "수면의자, 사무의자, 의자")
    List<String> hashtags;
}
