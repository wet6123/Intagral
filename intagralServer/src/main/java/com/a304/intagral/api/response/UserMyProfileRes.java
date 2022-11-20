package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.UserMyProfileDto;
import com.a304.intagral.db.dto.UserProfileDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "사용자 정보 Response")
@Getter
@Setter
public class UserMyProfileRes extends BaseResponseBody {
    @Schema(name = "닉네임", example = "goodman")
    String nickname;
    @Schema(name = "이미지 경로", example = "https://intad-bucket.s3.apd2b8cd5e8")
    String imgPath;
    @Schema(name = "소개글", example = "안녕하세요")
    String intro;
    static public UserMyProfileRes of(Integer statusCode, String message, UserMyProfileDto userMyProfileDto) {
        UserMyProfileRes res = new UserMyProfileRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setNickname(userMyProfileDto.getNickname());
        res.setImgPath(userMyProfileDto.getImgPath());
        res.setIntro(userMyProfileDto.getIntro());

        return res;
    }
}
