package com.a304.intagral.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FollowUserGetDto implements FollowListBase{

    String imagePath;
    String nickname;
    String intro;
    Integer followerCnt;
    @JsonProperty("is_follow")
    boolean isFollow;
}
