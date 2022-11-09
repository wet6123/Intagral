package com.a304.intagral.api.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "사용자 로그인 Request")
@Getter
@Setter
public class UserLoginPostReq {
	@Schema(name = "id token", example = "D64654DASD654")
	private String idToken;
}