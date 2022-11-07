package com.a304.intagral.api.controller;

import com.a304.intagral.api.response.FollowHashtagPostRes;
import com.a304.intagral.api.response.FollowUserPostRes;
import com.a304.intagral.api.response.PresetListRes;
import com.a304.intagral.api.service.FollowService;
import com.a304.intagral.common.auth.UserDetails;
import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.FollowHashtagPostDto;
import com.a304.intagral.db.dto.FollowUserPostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    FollowService followService;

    @PostMapping("/user/{nickname}")
    public ResponseEntity<? extends BaseResponseBody> updateUserFollow(Authentication authentication, @PathVariable String nickname){
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = userDetails.getId();
        try {
            FollowUserPostDto followUserPostDto = followService.toggleUserFollow(userId, nickname);

            return ResponseEntity.ok(FollowUserPostRes.of(200, "success", followUserPostDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @PostMapping("/hashtag/{hashtag}")
    public ResponseEntity<? extends BaseResponseBody> updateHashtagFollow(Authentication authentication, @PathVariable String hashtag){
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = userDetails.getId();
        try {
            FollowHashtagPostDto followHashtagPostDto = followService.toggleHashtagFollow(userId, hashtag);

            return ResponseEntity.ok(FollowHashtagPostRes.of(200, "success", followHashtagPostDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

}
