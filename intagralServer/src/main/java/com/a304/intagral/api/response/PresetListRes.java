package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.PresetListDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PresetListRes extends BaseResponseBody {

    @JsonProperty(value = "class")
    List<String> clsList;
    @JsonProperty(value = "data")
    List<PresetListDto> presetList = new ArrayList<>();

    public static PresetListRes of(int statusCode, String message, List<String> clsList, List<PresetListDto> presetList) {
        PresetListRes res = new PresetListRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setClsList(clsList);
        res.setPresetList(presetList);
        return res;
    }

}