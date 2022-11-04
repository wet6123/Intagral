package com.a304.intagral.api.service;

import com.a304.intagral.db.entity.Post;
import com.a304.intagral.db.repository.PostRepository;
import com.a304.intagral.dto.PostListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("postService")
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Override
    public Post getPostByPostId(Long postId) {
        return postRepository.findById(postId).get();
    }

    @Override
    public PostListDto getNewPostList(int page) {
        return null;
    }

    @Override
    public PostListDto getPostListByFollow(int page) {
        return null;
    }

    @Override
    public PostListDto getPostListByHashtag(String hashtag, int page) {
        return null;
    }

    @Override
    public PostListDto getPostListByNickname(String nickname, int page) {
        return null;
    }
}
