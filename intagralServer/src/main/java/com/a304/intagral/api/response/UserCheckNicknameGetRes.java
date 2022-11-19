package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCheckNicknameGetRes extends BaseResponseBody {

    @JsonProperty(value = "isAvailable")
    Integer isAvailable;

    public static UserCheckNicknameGetRes of(Integer statusCode, String message, Integer isAvailable) {
        UserCheckNicknameGetRes res = new UserCheckNicknameGetRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setIsAvailable(isAvailable);
        return res;
    }

}
