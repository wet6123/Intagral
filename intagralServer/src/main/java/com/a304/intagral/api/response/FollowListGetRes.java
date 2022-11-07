package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.FollowListBase;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FollowListGetRes<T extends FollowListBase> extends BaseResponseBody {
    @JsonProperty(value = "data")
    List<T> followList;

    static public FollowListGetRes of(int statusCode, String message, List<? extends FollowListBase> followList){
        FollowListGetRes res = new FollowListGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setFollowList(followList);

        return res;
    }

}
