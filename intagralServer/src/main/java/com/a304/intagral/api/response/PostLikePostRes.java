package com.a304.intagral.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLikePostRes {
    boolean isLike;
    Integer likeCnt;
}
