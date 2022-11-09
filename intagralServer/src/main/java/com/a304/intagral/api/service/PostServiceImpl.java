package com.a304.intagral.api.service;

import com.a304.intagral.api.request.PostAddPostReq;
import com.a304.intagral.api.response.PostAddPostRes;
import com.a304.intagral.api.response.PostLikePostRes;
import com.a304.intagral.common.response.FileDetail;
import com.a304.intagral.common.util.AmazonS3ResourceStorageUtil;
import com.a304.intagral.db.entity.*;
import com.a304.intagral.db.repository.*;
import com.a304.intagral.dto.PostDataDto;
import com.a304.intagral.dto.PostLikePostDto;
import com.a304.intagral.dto.PostListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


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
    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    AmazonS3ResourceStorageUtil amazonS3ResourceStorageUtil;

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
    public PostListDto getPostListByFollow(int page, Long userId) {
        User targetUser = userRepository.findById(userId).get();
        boolean isNext = false;

//      팔로우 중인 유저 모두 불러옴
        List<UserFollow> userFollowingList = targetUser.getUserFollowingList();
        List<Long> userIdList = new ArrayList<>();
        for (UserFollow userFollow : userFollowingList) {
            userIdList.add(userFollow.getUserIdTo().longValue());
        }

//      팔로우 중인 태그 모두  불러옴
        List<HashtagFollow> hashtagFollowList = targetUser.getHashtagFollowList();
        List<Long> hashtagIdList = new ArrayList<>();
        for (HashtagFollow hashtagFollow : hashtagFollowList) {
            hashtagIdList.add(hashtagFollow.getHashtagId().longValue());
        }

//      post 불러와서 유저 or 태그 포함하는지 조사
        List<Post> allList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<Post> postlist = new ArrayList<>();
        loop : for(Post post : allList){
            for(Long uid : userIdList){
                if(post.getUserId().intValue() == uid) {
                    postlist.add(post);
                    continue loop;
                }
            }
            for(Long hashtagId : hashtagIdList){
                List<PostHashtag> a = post.getPostHashtagList();
                for(PostHashtag tag : a){
                    if(tag.getHashtagId().intValue() == hashtagId){
                        postlist.add(post);
                        continue loop;
                    }
                }
            }
        }

        List<PostDataDto> list = new ArrayList<>();
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
        List<PostDataDto> list = new ArrayList<>();
        boolean isNext = false;

        Integer userId = userRepository.findByNickname(nickname).get().getId().intValue();
        List<Post> postlist = postRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "id"));

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
    public PostAddPostRes postAdd(Long userId, PostAddPostReq postAddPostReq) {
        MultipartFile multipartFile = postAddPostReq.getImage();
        FileDetail fileDetail = FileDetail.multipartOf(multipartFile);
        String resourceUrl = amazonS3ResourceStorageUtil.store(fileDetail.getPath(), multipartFile);

        Post post = Post.builder()
                .userId(userId.intValue())
                .postDate(LocalDateTime.now())
                .imgPath(resourceUrl)
                .likeCnt(0)
                .build();

        Post resPost = postRepository.save(post);

        Integer postId = resPost.getId().intValue();

        for (String tag : postAddPostReq.getHashtags()) {
            Hashtag hashtag = null;
            try{
                hashtag = hashtagRepository.findByContent(tag).get();
                //처음 등록되는 해시태그 추가
            } catch (NoSuchElementException e){
                hashtag = Hashtag.builder()
                        .content(tag)
                        .searchCnt(0)
                        .build();
                hashtag = hashtagRepository.save(hashtag);
            }

            PostHashtag postHashtag = PostHashtag.builder()
                    .postId(postId)
                    .hashtagId(hashtag.getId().intValue())
                    .build();

            postHashtagRepository.save(postHashtag);
        }

        PostAddPostRes res = new PostAddPostRes();
        res.setPostId(postId);
        return res;
    }

    @Override
    public PostLikePostDto postLike(Long userId, Long postId) {
        Integer cntLike = postLikeRepository.countByUserIdAndPostId(userId, postId);
        boolean isLike = cntLike != 0;

        if(isLike){
            postLikeRepository.deleteByUserIdAndPostId(userId, postId);
        }else{
            PostLike postLike = PostLike.builder()
                    .postId(postId)
                    .userId(userId)
                    .build();
            postLikeRepository.save(postLike);
        }

        PostLikePostDto postLikePostDto = PostLikePostDto.builder()
                .isLike(!isLike)
                .likeCnt(cntLike)
                .build();

        return postLikePostDto;
    }

    @Override
    public void postDelete(Long userId) {

    }
}
