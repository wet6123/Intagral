package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel("프리셋 리스트 Response")

@Getter
@Setter
public class PresetListRes extends BaseResponseBody {
    @ApiModelProperty(name = "대상 클래스 리스트", example = "default, chair, desk")
    @JsonProperty(value = "class")
    List<String> clsList;
    @ApiModelProperty(name = "프리셋 리스트", example = "허먼밀러, 4Legs")
    @JsonProperty(value = "data")
    Map<String, List<String>> presetList = new HashMap<>();

    public static PresetListRes of(int statusCode, String message, List<String> clsList, Map<String, List<String>> presetList) {
        PresetListRes res = new PresetListRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setClsList(clsList);
        res.setPresetList(presetList);
        return res;
    }

}