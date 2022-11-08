package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("auth Token Response")

@Getter
@Setter
public class UserLoginPostRes extends BaseResponseBody {
    @ApiModelProperty(name = "Auth Token", example = "E9DA4546CASADW21cxz...")
    String authToken;

    public static UserLoginPostRes of(Integer statusCode, String message, String accessToken) {
        UserLoginPostRes res = new UserLoginPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setAuthToken(accessToken);
        return res;
    }

}
