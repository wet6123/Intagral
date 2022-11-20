package com.a304.intagral.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProfileDto {
    String type;
    String nickname;
    String imgPath;
    String intro;
    Long following;
    Long follower;
    Long hashtag;
    @JsonProperty(value = "isFollow")
    boolean isFollow;
}
