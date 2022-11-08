package com.a304.intagral.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel("토큰 정보 Response")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRes {
    @ApiModelProperty(name = "엑세스 토큰")
    private String accessToken;
    @ApiModelProperty(name = "리프레시 토큰")
    private String refreshToken;

    public static TokenRes of(String accessToken, String refreshToken) {
        return TokenRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
