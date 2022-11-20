package com.a304.intagral.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDataDto {
    Long postId;
    String imgPath;
}
