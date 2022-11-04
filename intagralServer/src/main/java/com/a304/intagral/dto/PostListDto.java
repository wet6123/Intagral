package com.a304.intagral.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostListDto {
    List<PostData> data;
    int page;
    boolean isNext;
}
class PostData{
    int postId;
    String imgPath;
}

