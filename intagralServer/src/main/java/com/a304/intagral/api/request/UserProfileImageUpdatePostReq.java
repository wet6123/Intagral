package com.a304.intagral.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@ApiModel("사용자 프로필 이미지 업데이트 Request")
@Getter
@Setter
public class UserProfileImageUpdatePostReq {
    @ApiModelProperty(name = "프로필 이미지", example = "multipartfile")
    MultipartFile data;
}
