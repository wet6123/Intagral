package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel("인기 해시태그 Response")
@Getter
@Setter
public class HashtagHotListRes extends BaseResponseBody {
    @ApiModelProperty(name= "인기 태그 리스트")
    @JsonProperty(value = "data")
    List<String> hotList;

    public static HashtagHotListRes of(int statusCode, String message, List<String> hotList) {
        HashtagHotListRes res = new HashtagHotListRes();
        res.setStatusCode(statusCode);
        res.setHotList(hotList);
        return res;
    }

}
