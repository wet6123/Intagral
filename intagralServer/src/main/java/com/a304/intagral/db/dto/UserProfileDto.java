package com.a304.intagral.db.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProfileDto {
    String type;
    String nickname;
    String intro;
    Long following;
    Long follower;
    Long hashtag;
    boolean isFollow;
}
