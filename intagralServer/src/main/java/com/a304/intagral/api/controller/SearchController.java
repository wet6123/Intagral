package com.a304.intagral.api.controller;

import com.a304.intagral.api.request.SearchCntPostReq;
import com.a304.intagral.api.response.SearchRes;
import com.a304.intagral.api.service.SearchService;
import com.a304.intagral.common.auth.UserDetails;
import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.SearchHashtagDto;
import com.a304.intagral.db.dto.SearchUserDto;
import com.amazonaws.Response;
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

import java.util.List;

@Slf4j
@Schema(description = "검색 API")
@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @Operation(summary = "검색", description = "대상(사용자, 해시태그) 검색 결과를 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =  "success", content = @Content(schema = @Schema(implementation = SearchRes.class))),
            @ApiResponse(responseCode = "500", description =  "INTERNAL SERVER ERROR")
    })
    @GetMapping("")
    public ResponseEntity<? extends BaseResponseBody> searchId(@ApiIgnore Authentication authentication,
                                                               @ApiParam(value = "검색할 대상", example = "의자") @RequestParam(value = "q") String target) {
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

    @Operation(summary = "검색카운트 증가", description = "대상의 검색 카운트를 증가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description =  "success", content = @Content(schema = @Schema(implementation =  BaseResponseBody.class))),
            @ApiResponse(responseCode = "500", description =  "INTERNAL SERVER ERROR")
    })
    @PostMapping("")
    public ResponseEntity<? extends BaseResponseBody> searchCnt(@ApiParam(value = "해시태그명", example = "허먼밀러") @RequestBody SearchCntPostReq searchCntPostReq){
        try {
            searchService.countUpHashtagSearchCnt(searchCntPostReq.getHashtag());
            return ResponseEntity.ok(BaseResponseBody.of(200, "success"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }
}