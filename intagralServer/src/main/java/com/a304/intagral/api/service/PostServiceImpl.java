package com.a304.intagral.api.service;

import com.a304.intagral.db.entity.Post;
import com.a304.intagral.db.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("postService")
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Override
    public Post getPostByPostId(Long postId) {
        return postRepository.findByPostId(postId).get();
    }
}
