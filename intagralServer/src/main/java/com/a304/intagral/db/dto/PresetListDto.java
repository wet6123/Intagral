package com.a304.intagral.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PresetListDto {

    @JsonProperty(value = "class")
    String cls;
    @JsonProperty
    List<String> tagList;

}
