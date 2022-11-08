package com.a304.intagral.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("해시태그 검색 횟수 증가 Request")
@Getter
@Setter
public class SearchCntPostReq{
    @ApiModelProperty(name = "해시태그명", example = "사무의자")
    String hashtag;
}
