package com.a304.intagral.db.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchHashtagDto {
    String name;
    boolean isFollow;
}
