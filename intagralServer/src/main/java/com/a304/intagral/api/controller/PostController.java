package com.a304.intagral.api.controller;

import com.a304.intagral.api.request.PostAddPostReq;
import com.a304.intagral.api.response.FollowHashtagPostRes;
import com.a304.intagral.api.response.PostAddPostRes;
import com.a304.intagral.api.response.PostDetailRes;
import com.a304.intagral.api.response.PostLikePostRes;
import com.a304.intagral.api.service.PostService;
import com.a304.intagral.common.auth.UserDetails;
import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.FollowHashtagPostDto;
import com.a304.intagral.db.entity.Post;
import com.a304.intagral.db.entity.PostLike;
import com.a304.intagral.db.entity.User;
import com.a304.intagral.dto.PostDataDto;
import com.a304.intagral.dto.PostLikePostDto;
import com.a304.intagral.dto.PostListDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Schema(description = "게시글 API")
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostService postService;

    @Operation(summary = "게시글 상세 정보", description = "게시글에 대한 정보들을 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =  "success", content = @Content(schema = @Schema(implementation =  PostDetailRes.class))),
            @ApiResponse(responseCode = "500", description =  "INTERNAL SERVER ERROR")
    })
    @GetMapping("/{post_id}")
    public ResponseEntity<? extends BaseResponseBody> getPostDetail(@ApiIgnore Authentication authentication,
                                                                    @ApiParam(value = "게시글 번호", example = "4") @PathVariable("post_id") Long postId){
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

        boolean isFollow = postService.isFollowWriter(userId, user.getId());

        return ResponseEntity.ok(PostDetailRes.of(200, "success", imgPath, tags, likeCount, isLike, writer, writerImgPath, isFollow));
        } catch (RuntimeException e){
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @Operation(summary = "게시물 목록", description = "게시물을 최신순으로 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =  "success"),
            @ApiResponse(responseCode = "500", description =  "INTERNAL SERVER ERROR")
    })
    @GetMapping("/list")
    public ResponseEntity<?> newPostList(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "가져올 대상" , example = "[all | hashtag | user]") @RequestParam(value = "type") String type,
            @ApiParam(value = "페이지", example = "2") @RequestParam(value = "page") int page,
            @ApiParam(value = "유저 닉네임", example = "goodman") @RequestParam(value = "q", defaultValue="") String keyword)  {
        if((type.equals("hashtag") || type.equals("user")) &&  keyword.equals("")){
            throw new NullPointerException("type이 hashtag이거나 user일때는 키워드를 보내주어야 합니다.");
        }

        PostListDto res;

        if(type.equals("all")){
            res = postService.getNewPostList(page);
        }else if(type.equals("follow")){
            UserDetails userDetails = (UserDetails)authentication.getDetails();
            Long userId = Long.valueOf(userDetails.getUsername());
            res = postService.getPostListByFollow(page, userId);
        } else if (type.equals("hashtag")) {
            res = postService.getPostListByHashtag(keyword, page);
        } else if (type.equals("user")) {
            res = postService.getPostListByNickname(keyword, page);
        } else {
            throw new IllegalArgumentException(type + "은 type에 존재하지 않습니다.");
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "게시글 작성", description = "이미지와 해시태그로 게시물을 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =  "success"),
            @ApiResponse(responseCode = "500", description =  "INTERNAL SERVER ERROR")
    })
    @PostMapping("/publish")
    public ResponseEntity<?> addPost(@ApiIgnore Authentication authentication,
                                     @ApiParam(value = "이미지와 해시태그") PostAddPostReq postAddPostReq) {
        UserDetails userDetails = (UserDetails)authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());

        PostAddPostRes res = null;
        try {
           res = postService.postAdd(userId, postAddPostReq);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<?> likePost(Authentication authentication, @PathVariable(value = "postId") Long postId){
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = userDetails.getId();
        try {
            PostLikePostDto postLikePostDto = postService.postLike(userId, postId);

            return ResponseEntity.ok(PostLikePostRes.of(200, "success", postLikePostDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @PostMapping("/delete/{postId}")
    public ResponseEntity<?> deleteResponse(Authentication authentication, @PathVariable("postId") Long postId ) {
        UserDetails userDetails = (UserDetails)authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());

        try {
            String res = postService.postDelete(userId, postId);
            return ResponseEntity.ok( BaseResponseBody.of(200,res));
        }catch (RuntimeException e)
        {
            return ResponseEntity.
                    status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }
}
