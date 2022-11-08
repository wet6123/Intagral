package com.a304.intagral.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("사용자 로그인 Request")
@Getter
@Setter
public class UserLoginPostReq {
	@ApiModelProperty(name = "id token", example = "D64654DASD654")
	private String idToken;
}