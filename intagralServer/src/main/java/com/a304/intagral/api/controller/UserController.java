package com.a304.intagral.api.controller;

import com.a304.intagral.api.request.UserLoginPostReq;
import com.a304.intagral.api.request.UserProfileImageUpdatePostReq;
import com.a304.intagral.api.request.UserProfileUpdatePostReq;
import com.a304.intagral.api.response.*;
import com.a304.intagral.api.service.UserService;
import com.a304.intagral.common.auth.UserDetails;
import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.HashtagProfileDto;
import com.a304.intagral.db.dto.UserProfileDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Schema(description = "유저 API")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(summary = "로그인", description = "회원이 아니라면 회원가입을 시키고 로그인, AuthToken을 발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =  "success", content = @Content(schema = @Schema(implementation =  UserLoginPostRes.class))),
            @ApiResponse(responseCode = "500", description =  "INTERNAL SERVER ERROR")
    })
    @PostMapping("/login")
    public ResponseEntity<? extends BaseResponseBody> login(@ApiParam(value = "유저의 IdToken", example = "ED21365DAA...") @RequestBody UserLoginPostReq userLoginPostReq) {
        try {
            TokenRes tokenRes = userService.login(userLoginPostReq.getIdToken());
            log.debug("auth token: " + tokenRes.getAccessToken());
            return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", tokenRes.getAccessToken()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 처리")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =  "success", content = @Content(schema = @Schema(implementation =  BaseResponseBody.class))),
            @ApiResponse(responseCode = "500", description =  "INTERNAL SERVER ERROR")
    })
    @GetMapping("/logout")
    public ResponseEntity<? extends BaseResponseBody> logout(@ApiIgnore Authentication authentication) {
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

    @Operation(summary = "사용자 프로필", description = "사용자의 프로필(팔로잉, 팔로워, 해시태그팔로우, 게시글) 정보를 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =  "success",content = @Content(schema = @Schema(implementation =  UserProfileRes.class))),
            @ApiResponse(responseCode = "500", description =  "INTERNAL SERVER ERROR")
    })
    @GetMapping("/profile")
    public ResponseEntity<? extends BaseResponseBody> getUserProfile(@ApiIgnore Authentication authentication,
                                                                     @ApiParam(value = "사용자 닉네임", example = "goodman") @RequestParam(value = "q") String nickname) {
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        try {
            UserProfileDto userProfileDto = userService.getProfile(userId, nickname);

            return ResponseEntity.ok(UserProfileRes.of(200, "success", userProfileDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @Operation(summary = "사용자 소개글 업데이트", description = "사용자의 소개글을 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =  "success", content = @Content(schema = @Schema(implementation =  BaseResponseBody.class))),
            @ApiResponse(responseCode = "500", description =  "INTERNAL SERVER ERROR")
    })
    @PostMapping("/profile/info")
    public ResponseEntity<? extends BaseResponseBody> updateProfile(@ApiIgnore Authentication authentication,
                                                                    @ApiParam(value = "변경할 소개글", example = "안녕하세요 반갑습니다.") @RequestBody UserProfileUpdatePostReq userProfileUpdatePostReq) {
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        try{
            userService.updateProfile(userId, userProfileUpdatePostReq);

            return ResponseEntity.ok(BaseResponseBody.of(200, "success"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @Operation(summary = "사용자 이미지 업데이트", description = "사용자의 프로필사진 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =  "success", content = @Content(schema = @Schema(implementation =  BaseResponseBody.class))),
            @ApiResponse(responseCode = "500", description =  "INTERNAL SERVER ERROR")
    })
    @PostMapping("/profile/image")
    public ResponseEntity<? extends BaseResponseBody> updateProfileImage(@ApiIgnore Authentication authentication,
                                                                         @ApiParam(value = "변경할 이미지", example = "[multipartFile]") UserProfileImageUpdatePostReq userProfileImageUpdatePostReq) {
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        try{
            userService.updateProfileImage(userId, userProfileImageUpdatePostReq);

            return ResponseEntity.ok(BaseResponseBody.of(200, "success"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<? extends BaseResponseBody> checkNicknameDuplication(@RequestParam String nickname) {
        try{
            boolean isAvailable = userService.checkNicknameDuplication(nickname);

            return ResponseEntity.ok(UserCheckNicknameGetRes.of(200, "success", isAvailable));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

}