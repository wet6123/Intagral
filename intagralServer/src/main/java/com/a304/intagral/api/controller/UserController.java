package com.a304.intagral.api.controller;

import com.a304.intagral.api.request.UserLoginPostReq;
import com.a304.intagral.api.request.UserProfileUpdatePostReq;
import com.a304.intagral.api.response.HashtagProfileRes;
import com.a304.intagral.api.response.TokenRes;
import com.a304.intagral.api.response.UserLoginPostRes;
import com.a304.intagral.api.response.UserProfileRes;
import com.a304.intagral.api.service.UserService;
import com.a304.intagral.common.auth.UserDetails;
import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.HashtagProfileDto;
import com.a304.intagral.db.dto.UserProfileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<? extends BaseResponseBody> login(@RequestBody UserLoginPostReq userLoginPostReq) {
        try {
            TokenRes tokenRes = userService.login(userLoginPostReq.getIdToken());
            log.debug("auth token: " + tokenRes.getAccessToken());
            return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", tokenRes.getAccessToken()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<? extends BaseResponseBody> logout(Authentication authentication) {
        log.debug("logout Controller");
        //로그인 처리
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        try {
            userService.logout(userId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }

    @GetMapping("/profile")
    public ResponseEntity<? extends BaseResponseBody> getUserProfile(Authentication authentication, @RequestParam(value = "q") String nickname) {
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        try {
            UserProfileDto userProfileDto = userService.getProfile(userId, nickname);

            return ResponseEntity.ok(UserProfileRes.of(200, "success", userProfileDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @PostMapping("/profile/info")
    public ResponseEntity<? extends BaseResponseBody> updateProfile(Authentication authentication, @RequestBody UserProfileUpdatePostReq userProfileUpdatePostReq) {
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        try{
            userService.updateProfile(userId, userProfileUpdatePostReq);

            return ResponseEntity.ok(BaseResponseBody.of(200, "success"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }

    }
}