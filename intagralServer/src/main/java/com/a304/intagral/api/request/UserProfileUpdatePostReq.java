package com.a304.intagral.api.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "사용자 소개글 업데이트 Request")
@Getter
@Setter
public class UserProfileUpdatePostReq {
    @Schema(name = "변경할 대상", allowableValues = {"nickname", "intro"})
    String type;
    @Schema(name = "변경 내용", example = "김테스트")
    String data;
}
