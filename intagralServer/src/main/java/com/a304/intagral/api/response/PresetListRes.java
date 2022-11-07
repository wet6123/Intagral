package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PresetListRes extends BaseResponseBody {

    @JsonProperty(value = "class")
    List<String> clsList;
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