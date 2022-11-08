package com.a304.intagral.api.service;


import com.a304.intagral.api.request.PostAddPostReq;
import com.a304.intagral.api.response.PostAddPostRes;
import com.a304.intagral.api.response.PostLikePostRes;
import com.a304.intagral.db.entity.Post;
import com.a304.intagral.db.entity.PostLike;
import com.a304.intagral.db.entity.User;
import com.a304.intagral.dto.PostListDto;

import java.util.List;

public interface PostService {
//  게시글 상세 가져오기
    public Post getPostByPostId(Long PostId);
//  게시글의 태그 목록 가져오기
    public List<String> getTagsByPostId(Long PostId);
//  게시글의 좋아요 목록 가져오기
    public List<PostLike> getLike(Long PostId);
//  게시글 작성자 정보 가져오기
    public User getUserByPostId(Long PostId);

// 최신 게시글 목록
    public PostListDto getNewPostList(int page);
// 팔로우 정보로 게시글 목록 받기
    public  PostListDto getPostListByFollow(int page, Long userId);
// 해시태그로 게시글 목록 받기
    public  PostListDto getPostListByHashtag(String hashtag, int page);
// 닉네임으로 게시글 목록 받기 (개인 페이지 안 게시글 목록)
    public PostListDto getPostListByNickname(String nickname, int page);
// 게시글 쓰기
    public PostAddPostRes postAdd(Long userId, PostAddPostReq postAddPostReq);
// 게시글 좋아요
    public PostLikePostRes postLike(Long userId);
// 게시글 삭제
    public void postDelete(Long userId);
}
