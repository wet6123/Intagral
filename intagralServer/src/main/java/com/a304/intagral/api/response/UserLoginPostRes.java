package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginPostRes extends BaseResponseBody {

    String authToken;

    public static UserLoginPostRes of(Integer statusCode, String message, String accessToken) {
        UserLoginPostRes res = new UserLoginPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setAuthToken(accessToken);
        return res;
    }

}
