package com.a304.intagral.api.controller;

import com.a304.intagral.api.request.PresetAddPostReq;
import com.a304.intagral.api.request.PresetDeletePostReq;
import com.a304.intagral.api.response.PresetListRes;
import com.a304.intagral.api.service.PresetService;
import com.a304.intagral.common.auth.UserDetails;
import com.a304.intagral.common.response.BaseResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Api("프리셋 API")
@RestController
@RequestMapping("/api/preset")
public class PresetController {

    @Autowired
    PresetService presetService;

    @Operation(summary = "사용자의 프리셋 목록", description = "현재 로그인한 사용자의 프리셋을 반환")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/list")
    public ResponseEntity<? extends BaseResponseBody> logout(@ApiIgnore Authentication authentication,
                                                             @ApiParam(value = "검색 범위", example = "[all | class]") @RequestParam String type,
                                                             @ApiParam(value = "검색할 대상", example = "leg")@RequestParam(required = false, defaultValue = "all") String q) {

        UserDetails userDetails = (UserDetails)authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        Map<String, List<String>> presetList = null;
        List<String> classList = new ArrayList<>();
        try {
            // 리스트 가져오기
            if("all".equals(type)){
                classList.addAll(presetService.getAllClsName());
            } else{
                classList.add("default");
                classList.add(q);
            }
            presetList = presetService.getAllPreset(userId, classList);

            return ResponseEntity.ok(PresetListRes.of(200, "success", classList, presetList));
        } catch(RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @Operation(summary = "프리셋 추가", description = "프리셋에 태그를 추가")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/add")
    public ResponseEntity<? extends  BaseResponseBody> addPreset(@ApiIgnore Authentication authentication,
                                                                 @ApiParam(value = "추가할 태그정보", example = "이케아책상") @RequestBody PresetAddPostReq presetAddPostReq){
        UserDetails userDetails = (UserDetails)authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        try{
            presetService.addPreset(userId, presetAddPostReq);

            return ResponseEntity.ok(PresetListRes.of(200, "success"));
        } catch(RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }
    @Operation(summary = "프리셋 삭제", description = "프리셋에서 태그를 삭제")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
    })

    @PostMapping("/delete")
    public ResponseEntity<? extends  BaseResponseBody> deleteTag(@ApiIgnore Authentication authentication,
                                                                 @ApiParam(value = "삭제할 태그정보", example = "chair") @RequestBody PresetDeletePostReq presetDeletePostReq){
        UserDetails userDetails = (UserDetails)authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        try{
            presetService.deletePreset(userId, presetDeletePostReq);

            return ResponseEntity.ok(PresetListRes.of(200, "success"));
        } catch(RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }
}