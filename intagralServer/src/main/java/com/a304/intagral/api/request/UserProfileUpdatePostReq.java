package com.a304.intagral.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("사용자 소개글 업데이트 Request")
@Getter
@Setter
public class UserProfileUpdatePostReq {
    @ApiModelProperty(name = "변경할 대상", example = "[nickname, intro]")
    String type;
    @ApiModelProperty(name = "변경 내용", example = "김테스트")
    String data;
}
