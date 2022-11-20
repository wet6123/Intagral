package com.a304.intagral.api.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "토큰 정보 Response")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRes {
    @Schema(name = "엑세스 토큰")
    private String accessToken;
    @Schema(name = "리프레시 토큰")
    private String refreshToken;

    public static TokenRes of(String accessToken, String refreshToken) {
        return TokenRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
