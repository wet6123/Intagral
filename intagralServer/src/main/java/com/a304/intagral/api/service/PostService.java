package com.a304.intagral.api.service;

import com.a304.intagral.db.entity.Post;

public interface PostService {
//  게시글 디테일 가져오기
    public Post getPostByPostId(Long PostId);
}
