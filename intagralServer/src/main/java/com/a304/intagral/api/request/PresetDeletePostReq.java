package com.a304.intagral.api.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "프리셋 삭제 Request")
@Getter
@Setter
public class PresetDeletePostReq {
    @Schema(name = "추가할 프리셋", example = "chair")
    String cls;
    @Schema(name = "태그", example = "걸상")
    String data;
}