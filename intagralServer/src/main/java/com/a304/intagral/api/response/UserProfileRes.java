package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.UserProfileDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("사용자 프로필 Response")

@Getter
@Setter
public class UserProfileRes extends BaseResponseBody {
    @ApiModelProperty(name = "대상", example = "[user | hashtag]")
    String type;
    @ApiModelProperty(name = "닉네임", example = "goodman")
    String nickname;
    @ApiModelProperty(name = "이미지 경로", example = "https://intad-bucket.s3.apd2b8cd5e8")
    String imgPath;
    @ApiModelProperty(name = "소개글", example = "안녕하세요")
    String intro;
    @ApiModelProperty(name = "팔로잉 수", example = "425")
    Long following;
    @ApiModelProperty(name = "팔로워 수", example = "21")
    Long follower;
    @ApiModelProperty(name = "팔로잉 하고 있는 해시태그 수", example = "2555")
    Long hashtag;
    @JsonProperty(value = "isFollow")
    boolean isFollow;

    static public UserProfileRes of(Integer statusCode, String message, UserProfileDto userProfileDto) {
        UserProfileRes res = new UserProfileRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setType("user");
        res.setNickname(userProfileDto.getNickname());
        res.setImgPath(userProfileDto.getImgPath());
        res.setIntro(userProfileDto.getIntro());
        res.setFollowing(userProfileDto.getFollowing());
        res.setFollower(userProfileDto.getFollower());
        res.setHashtag(userProfileDto.getHashtag());
        res.setFollow(userProfileDto.isFollow());

        return res;
    }
}
