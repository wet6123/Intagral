package com.a304.intagral.api.service;


import com.a304.intagral.dto.PostDto;

public interface PostService {
//  게시글 디테일 가져오기
    public PostDto getPostByPostId(Long PostId);
}
