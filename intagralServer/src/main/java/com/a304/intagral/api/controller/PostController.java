package com.a304.intagral.api.controller;

import com.a304.intagral.api.service.PostService;
import com.a304.intagral.dto.PostDto;
import com.a304.intagral.dto.PostListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping("/{post_id}")
    public ResponseEntity<PostDto> getPostDetail(@PathVariable("post_id") Long postId){
        PostDto postDetail = postService.getPostByPostId(postId);
        return new ResponseEntity<>(postDetail, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> newPostList(@RequestParam(value = "type") String type,
                                         @RequestParam(value = "page") int page,
                                         @RequestParam(value = "q", defaultValue="") String keyword)  {
        if((type.equals("hashtag") || type.equals("user")) &&  keyword.equals("")){
            throw new NullPointerException("type이 hashtag이거나 user일때는 키워드를 보내주어야 합니다.");
        }

        PostListDto postList = null;

        if(type.equals("all")){
            postList = postService.getNewPostList(page);
        }else if(type.equals("follow")){
            postList = postService.getPostListByFollow(page);
        } else if (type.equals("hashtag")) {
            postList = postService.getPostListByHashtag(keyword, page);
        } else if (type.equals("user")) {
            postList = postService.getPostListByNickname(keyword, page);
        } else {
            throw new IllegalArgumentException(type + "은 type에 존재하지 않습니다.");
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }
}
