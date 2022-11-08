package com.a304.intagral.api.controller;

import com.a304.intagral.api.request.SearchCntPostReq;
import com.a304.intagral.api.response.SearchRes;
import com.a304.intagral.api.service.SearchService;
import com.a304.intagral.common.auth.UserDetails;
import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.SearchHashtagDto;
import com.a304.intagral.db.dto.SearchUserDto;
import com.amazonaws.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @GetMapping("")
    public ResponseEntity<? extends BaseResponseBody> searchId(Authentication authentication, @RequestParam(value = "q") String target) {
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        try {
            List<SearchUserDto> users = searchService.searchUser(userId, target);
            List<SearchHashtagDto> hashtags = searchService.searchHashtag(userId, target);

            return ResponseEntity.ok(SearchRes.of(200, "success", users, hashtags));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @PostMapping("")
    public ResponseEntity<? extends BaseResponseBody> searchCnt(@RequestBody SearchCntPostReq searchCntPostReq){
        try {
            searchService.countUpHashtagSearchCnt(searchCntPostReq.getHashtag());
            return ResponseEntity.ok(BaseResponseBody.of(200, "success"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }
}