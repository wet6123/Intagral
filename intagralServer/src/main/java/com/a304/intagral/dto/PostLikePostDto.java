package com.a304.intagral.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostLikePostDto {
    @JsonProperty(value = "isLike")
    boolean isLike;
    @JsonProperty(value = "likeCnt")
    Integer likeCnt;
}
