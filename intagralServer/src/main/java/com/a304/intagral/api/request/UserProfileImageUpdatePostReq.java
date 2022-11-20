package com.a304.intagral.api.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "사용자 프로필 이미지 업데이트 Request")
@Getter
@Setter
public class UserProfileImageUpdatePostReq {
    @Schema(name = "프로필 이미지", example = "multipartfile")
    MultipartFile data;
}
