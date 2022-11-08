package com.a304.intagral.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("프리셋 추가 Request")
@Getter
@Setter
public class PresetAddPostReq {
    @ApiModelProperty(name = "추가할 프리셋", example = "chair")
    String cls;
    @ApiModelProperty(name = "태그", example = "걸상")
    String data;
}