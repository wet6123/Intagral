package com.a304.intagral.api.controller;

import com.a304.intagral.api.response.FollowHashtagPostRes;
import com.a304.intagral.api.response.FollowListGetRes;
import com.a304.intagral.api.response.FollowUserPostRes;
import com.a304.intagral.api.response.PresetListRes;
import com.a304.intagral.api.service.FollowService;
import com.a304.intagral.common.auth.UserDetails;
import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.FollowHashtagPostDto;
import com.a304.intagral.db.dto.FollowListBase;
import com.a304.intagral.db.dto.FollowUserPostDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@Api("팔로우 API")
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    FollowService followService;

    @Operation(summary = "사용자 팔로우 토글", description = "대상 사용자를 팔로우/언팔로우")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/hello")
    @PostMapping("/user/{nickname}")
    public ResponseEntity<? extends BaseResponseBody> updateUserFollow(@ApiIgnore Authentication authentication,
                                                                       @ApiParam(value="대상 사용자의 닉네임", required = true, example = "goodman") @PathVariable String nickname){
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = userDetails.getId();
        try {
            FollowUserPostDto followUserPostDto = followService.toggleUserFollow(userId, nickname);

            return ResponseEntity.ok(FollowUserPostRes.of(200, "success", followUserPostDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @Operation(summary = "해시태그 팔로우 토글", description = "대상 해시태그를 팔로우/언팔로우")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/hashtag/{hashtag}")
    public ResponseEntity<? extends BaseResponseBody> updateHashtagFollow(@ApiIgnore Authentication authentication,
                                                                          @ApiParam(value = "대상 해시태그 이름", required = true, example = "desker") @PathVariable String hashtag){
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = userDetails.getId();
        try {
            FollowHashtagPostDto followHashtagPostDto = followService.toggleHashtagFollow(userId, hashtag);

            return ResponseEntity.ok(FollowHashtagPostRes.of(200, "success", followHashtagPostDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @Operation(summary = "사용자의 팔로우 리스트", description = "팔로잉, 팔로워, 해시태그 리스트를 type에 맞춰 반환")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/list/user")
    public ResponseEntity<? extends BaseResponseBody> getUserFollowList(@ApiIgnore Authentication authentication,
                                                                        @ApiParam(value = "가져올 리스트 종류", example = "[following | follower | hashtag]") @RequestParam String type,
                                                                        @ApiParam(value = "대상의 이름", example = "goodman") @RequestParam("q") String nickname){
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = userDetails.getId();
        List<? extends FollowListBase> followList = null;
        try {
            if("following".equals(type)){
                followList = followService.getUserFollowingList(userId, nickname);
            } else if ("follower".equals(type)) {
                followList = followService.getFollowerList(userId, nickname);
            } else {
                followList = followService.getHashtagFollowingList(userId, nickname);
            }

            return ResponseEntity.ok(FollowListGetRes.of(200, "success", followList));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @Operation(summary = "해시태그 팔로워 리스트", description = "해시태그의 팔로워 리스트를 반환")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/list/hashtag")
    public ResponseEntity<? extends BaseResponseBody> getHashtagFollowList(@ApiIgnore Authentication authentication,
                                                                           @ApiParam(value = "가져올 리스트 종류", example = "follower") @RequestParam String type,
                                                                           @ApiParam(value = "태그의 이름", example = "desker") @RequestParam("q") String hashtag){
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = userDetails.getId();
        List<? extends FollowListBase> followList = null;
        try {
                followList = followService.getHashtagFollowerList(userId, hashtag);

            return ResponseEntity.ok(FollowListGetRes.of(200, "success", followList));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }
}
