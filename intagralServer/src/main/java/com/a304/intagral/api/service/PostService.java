package com.a304.intagral.api.service;


import com.a304.intagral.db.entity.Post;
import com.a304.intagral.dto.PostListDto;

import java.util.Optional;

public interface PostService {
//  게시글 상세 가져오기
    public Post getPostByPostId(Long PostId);

// 최신 게시글 목록
    public PostListDto getNewPostList(int page);
// 팔로우 정보로 게시글 목록 받기
    public  PostListDto getPostListByFollow(int page);
// 해시태그로 게시글 목록 받기
    public  PostListDto getPostListByHashtag(String hashtag, int page);
// 닉네임으로 게시글 목록 받기 (개인 페이지 안 게시글 목록)
    public PostListDto getPostListByNickname(String nickname, int page);
}
