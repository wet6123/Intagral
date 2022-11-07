package com.a304.intagral.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostListDto {
    List<PostDataDto> data;
    int page;
    @JsonProperty(value = "isNext")
    boolean isNext;
}

