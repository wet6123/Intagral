package com.a304.intagral.api.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "해시태그 검색 횟수 증가 Request")
@Getter
@Setter
public class SearchCntPostReq{
    @Schema(name = "해시태그명", example = "사무의자")
    String hashtag;
}
