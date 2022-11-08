package com.a304.intagral.api.controller;

import com.a304.intagral.api.request.PresetAddPostReq;
import com.a304.intagral.api.request.PresetDeletePostReq;
import com.a304.intagral.api.response.PresetListRes;
import com.a304.intagral.api.service.PresetService;
import com.a304.intagral.common.auth.UserDetails;
import com.a304.intagral.common.response.BaseResponseBody;
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
@RestController
@RequestMapping("/api/preset")
public class PresetController {

    @Autowired
    PresetService presetService;

    @GetMapping("/list")
    public ResponseEntity<? extends BaseResponseBody> logout(@ApiIgnore Authentication authentication, @RequestParam String type, @RequestParam(required = false, defaultValue = "all") String q) {

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

    @PostMapping("/add")
    public ResponseEntity<? extends  BaseResponseBody> addPreset(@ApiIgnore Authentication authentication, @RequestBody PresetAddPostReq presetAddPostReq){
        UserDetails userDetails = (UserDetails)authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        try{
            presetService.addPreset(userId, presetAddPostReq);

            return ResponseEntity.ok(PresetListRes.of(200, "success"));
        } catch(RuntimeException e) {
            return ResponseEntity.status(500).body(BaseResponseBody.of(500, e.getMessage()));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<? extends  BaseResponseBody> deleteTag(@ApiIgnore Authentication authentication, @RequestBody PresetDeletePostReq presetDeletePostReq){
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