package com.a304.intagral.api.controller;

import com.a304.intagral.api.response.HashtagHotListRes;
import com.a304.intagral.api.response.HashtagProfileRes;
import com.a304.intagral.api.response.SearchRes;
import com.a304.intagral.api.service.HashtagService;
import com.a304.intagral.api.service.SearchService;
import com.a304.intagral.common.auth.UserDetails;
import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.HashtagProfileDto;
import com.a304.intagral.db.dto.SearchHashtagDto;
import com.a304.intagral.db.dto.SearchUserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@Api("해시태그 API")
@RestController
@RequestMapping("/api/hashtag")
public class HashtagController {

    @Autowired
    HashtagService hashTagService;

    @Operation(summary = "해시태그 프로필", description = "해시태그의 프로필(팔로워, 게시글) 정보를 반환")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/profile")
    public ResponseEntity<? extends BaseResponseBody> searchId(@ApiIgnore Authentication authentication,
                                                               @ApiParam(value = "대상 해시태그명", example = "허먼밀러") @RequestParam(value = "q") String target) {
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        try {
            HashtagProfileDto hashtagProfileDto = hashTagService.getProfile(userId, target);

            return ResponseEntity.ok(HashtagProfileRes.of(200, "success", hashtagProfileDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @Operation(summary = "인기있는 해시태그 리스트", description = "검색 횟수가 많은 5개의 해시태그 반환")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/list/hot")
    public ResponseEntity<? extends BaseResponseBody> getHotList(){
        try{
            List<String> hotList = hashTagService.getHotList();

            return ResponseEntity.ok(HashtagHotListRes.of(200, "success", hotList));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }
}