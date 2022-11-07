package com.a304.intagral.api.controller;

import com.a304.intagral.api.response.PostDetailRes;
import com.a304.intagral.api.service.PostService;
import com.a304.intagral.common.auth.UserDetails;
import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.entity.Post;
import com.a304.intagral.db.entity.PostLike;
import com.a304.intagral.db.entity.User;
import com.a304.intagral.dto.PostDataDto;
import com.a304.intagral.dto.PostListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping("/{post_id}")
    public ResponseEntity<? extends BaseResponseBody> getPostDetail(Authentication authentication, @PathVariable("post_id") Long postId){
        UserDetails userDetails = (UserDetails)authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());

        try{

//      포스트 관련 정보
        Post post = postService.getPostByPostId(postId);
        List<String> tags = postService.getTagsByPostId(postId);
        String imgPath = post.getImgPath();

//      좋아요 정보
        List<PostLike> likeList = postService.getLike(postId);
        Long likeCount = Long.valueOf(likeList.size());
        boolean isLike = false;
        for(PostLike like : likeList){
            if(like.getUserId() == userId){
                isLike = true;
            }
        }

//      글쓴 유저 정보
        User user = postService.getUserByPostId(postId);
        String writer = user.getNickname();
        String writerImgPath = user.getProfileImgPath();

        return ResponseEntity.ok(PostDetailRes.of(200, "success", imgPath, tags, likeCount, isLike, writer, writerImgPath));
        } catch (RuntimeException e){
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> newPostList(@RequestParam(value = "type") String type,
                                         @RequestParam(value = "page") int page,
                                         @RequestParam(value = "q", defaultValue="") String keyword)  {
        if((type.equals("hashtag") || type.equals("user")) &&  keyword.equals("")){
            throw new NullPointerException("type이 hashtag이거나 user일때는 키워드를 보내주어야 합니다.");
        }

        PostListDto res;

        if(type.equals("all")){
            res = postService.getNewPostList(page);
        }else if(type.equals("follow")){
            res = postService.getPostListByFollow(page);
        } else if (type.equals("hashtag")) {
            res = postService.getPostListByHashtag(keyword, page);
        } else if (type.equals("user")) {
            res = postService.getPostListByNickname(keyword, page);
        } else {
            throw new IllegalArgumentException(type + "은 type에 존재하지 않습니다.");
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
