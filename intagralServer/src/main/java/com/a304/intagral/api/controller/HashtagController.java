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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/hashtag")
public class HashtagController {

    @Autowired
    HashtagService hashTagService;

    @GetMapping("/profile")
    public ResponseEntity<? extends BaseResponseBody> searchId(Authentication authentication, @RequestParam(value = "q") String target) {
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        try {
            HashtagProfileDto hashtagProfileDto = hashTagService.getProfile(userId, target);

            return ResponseEntity.ok(HashtagProfileRes.of(200, "success", hashtagProfileDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

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