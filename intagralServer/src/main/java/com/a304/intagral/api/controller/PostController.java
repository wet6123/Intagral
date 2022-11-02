package com.a304.intagral.api.controller;

import com.a304.intagral.api.service.PostService;
import com.a304.intagral.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
