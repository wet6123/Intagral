package com.a304.intagral.api.controller;

import com.a304.intagral.api.response.PresetListRes;
import com.a304.intagral.api.service.PresetService;
import com.a304.intagral.common.auth.UserDetails;
import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.PresetListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/preset")
public class PresetController {

    @Autowired
    PresetService presetService;

    @GetMapping("/list")
    public ResponseEntity<? extends BaseResponseBody> logout(Authentication authentication, @RequestParam String type, @RequestParam(required = false, defaultValue = "all") String q) {

        UserDetails userDetails = (UserDetails)authentication.getDetails();
        Long userId = Long.valueOf(userDetails.getUsername());
        List<PresetListDto> presetList = null;
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
}