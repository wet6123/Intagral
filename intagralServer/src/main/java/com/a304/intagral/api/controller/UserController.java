package com.a304.intagral.api.controller;

import com.a304.intagral.api.request.UserLoginPostReq;
import com.a304.intagral.api.response.TokenRes;
import com.a304.intagral.api.response.UserLoginPostRes;
import com.a304.intagral.api.service.UserService;
import com.a304.intagral.common.response.BaseResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<? extends BaseResponseBody> login(@RequestBody UserLoginPostReq userLoginPostReq) {
        try {
            TokenRes tokenRes = userService.login(userLoginPostReq.getIdToken());
            log.debug("auth token: "+tokenRes.getAccessToken());
            return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", tokenRes.getAccessToken()));
        } catch(RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, "Internal Server Error"));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<? extends BaseResponseBody> logout() {
        log.debug("logout Controller");
        //로그인 처리
        return ResponseEntity.ok(BaseResponseBody.of(200, "Success"));
    }
}