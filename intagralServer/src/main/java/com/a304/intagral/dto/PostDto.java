package com.a304.intagral.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDto {
    String imgPath;
    String[] tags;
    int likeCnt;
    boolean isLike;
    String writer;
    String writerImgPath;
}
