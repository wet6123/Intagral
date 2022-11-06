package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HashtagHotListRes extends BaseResponseBody {
    @JsonProperty(value = "data")
    List<String> hotList;

    public static HashtagHotListRes of(int statusCode, String message, List<String> hotList) {
        HashtagHotListRes res = new HashtagHotListRes();
        res.setStatusCode(statusCode);
        res.setHotList(hotList);
        return res;
    }

}
