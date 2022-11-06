package com.a304.intagral.api.service;

import com.a304.intagral.db.entity.Post;
import com.a304.intagral.db.entity.PostHashtag;
import com.a304.intagral.db.entity.PostLike;
import com.a304.intagral.db.entity.User;
import com.a304.intagral.db.repository.PostHashtagRepository;
import com.a304.intagral.db.repository.PostRepository;
import com.a304.intagral.db.repository.UserRepository;
import com.a304.intagral.dto.PostListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("postService")
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostHashtagRepository postHashtagRepository;

    @Override
    public Post getPostByPostId(Long PostId) {
        return postRepository.findById(PostId).get();
    }

    @Override
    public List<String> getTagsByPostId(Long PostId) {
        List<PostHashtag> postHashtagList = postRepository.findById(PostId).get().getPostHashtagList();
        List<String> res = new ArrayList<>();
        for(PostHashtag postHashtag : postHashtagList){
            res.add(postHashtag.getPostHashtagToHashtag().getContent());
        }
        return res;
    }

    @Override
    public List<PostLike> getLike(Long PostId) {
        List<PostLike> res = postRepository.findById(PostId).get().getPostLikeList();
        return res;
    }

    @Override
    public User getUserByPostId(Long PostId) {
        User user = postRepository.findById(PostId).get().getPostToUser();
        return user;
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
