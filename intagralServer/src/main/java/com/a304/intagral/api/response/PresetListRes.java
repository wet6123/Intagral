package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Schema(description = "프리셋 리스트 Response")
@Getter
@Setter
public class PresetListRes extends BaseResponseBody {
    @Schema(name = "대상 클래스 리스트", example = "default, chair, desk")
    @JsonProperty(value = "class")
    List<String> clsList;
    @Schema(name = "한국어 대상 클래스 리스트", example = "기본, 의자, 책상")
    @JsonProperty(value = "classKor")
    List<String> clsKorList;
    @Schema(name = "프리셋 리스트", example = "허먼밀러, 4Legs")
    @JsonProperty(value = "data")
    Map<String, List<String>> presetList = new HashMap<>();

    public static PresetListRes of(int statusCode, String message, List<String> clsList, List<String> clsKorList, Map<String, List<String>> presetList) {
        PresetListRes res = new PresetListRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setClsList(clsList);
        res.setClsKorList(clsKorList);
        res.setPresetList(presetList);
        return res;
    }

}