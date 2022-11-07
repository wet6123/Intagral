package com.a304.intagral.api.service;

import com.a304.intagral.db.entity.*;
import com.a304.intagral.db.repository.HashtagRepository;
import com.a304.intagral.db.repository.PostHashtagRepository;
import com.a304.intagral.db.repository.PostRepository;
import com.a304.intagral.db.repository.UserRepository;
import com.a304.intagral.dto.PostDataDto;
import com.a304.intagral.dto.PostDto;
import com.a304.intagral.dto.PostListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("postService")
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostHashtagRepository postHashtagRepository;
    @Autowired
    HashtagRepository hashtagRepository;

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
        List<PostDataDto> list = new ArrayList<>();
        List<Post> postlist = postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        boolean isNext = false;

        int len = postlist.size();
        if(len - (page-1) * 10 > 0){
            if(len - page * 10 > 10){
                for(int i = (page-1)*10; i < page*10; i++){
                    Post post = postlist.get(i);
                    PostDataDto postData = PostDataDto.builder()
                            .postId(post.getId())
                            .imgPath(post.getImgPath())
                            .build();
                    list.add(postData);
                    isNext = true;
                }
            }else {
                for(int i = (page-1)*10; i < (page-1)*10 + len%10; i++){
                    Post post = postlist.get(i);
                    PostDataDto postData = PostDataDto.builder()
                            .postId(post.getId())
                            .imgPath(post.getImgPath())
                            .build();
                    list.add(postData);
                    isNext = false;
                }
            }
        }

        PostListDto res = PostListDto.builder()
                .data(list)
                .page(page)
                .isNext(isNext)
                .build();
        return res;
    }

    @Override
    public PostListDto getPostListByFollow(int page) {
        return null;
    }

    @Override
    public PostListDto getPostListByHashtag(String hashtag, int page) {
        List<PostDataDto> list = new ArrayList<>();
        Integer hashtagId = hashtagRepository.findByContent(hashtag).get().getId().intValue();
        List<PostHashtag> postHashtagList = postHashtagRepository.findByHashtagId(hashtagId, Sort.by(Sort.Direction.DESC, "id"));
        boolean isNext = false;

        List<PostDataDto> postlist = new ArrayList<>();
        for(PostHashtag postHashtag : postHashtagList){
            Post post = postHashtag.getPostHashtagToPost();

            PostDataDto data = PostDataDto.builder()
                    .postId(post.getId())
                    .imgPath(post.getImgPath())
                    .build();

            postlist.add(data);
        }

        int len = postlist.size();
        if(len - (page-1) * 10 > 0){
            if(len - page * 10 > 10){
                for(int i = (page-1)*10; i < page*10; i++){
                    list.add(postlist.get(i));
                    isNext = true;
                }
            }else {
                for(int i = (page-1)*10; i < (page-1)*10 + len%10; i++){
                    list.add(postlist.get(i));
                    isNext = false;
                }
            }
        }

        PostListDto res = PostListDto.builder()
                .data(list)
                .page(page)
                .isNext(isNext)
                .build();
        return res;
    }

    @Override
    public PostListDto getPostListByNickname(String nickname, int page) {
        return null;
    }
}
