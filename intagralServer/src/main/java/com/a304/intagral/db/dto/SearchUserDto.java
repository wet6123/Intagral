package com.a304.intagral.db.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchUserDto {
    String name;
    String profileImage;
    boolean isFollow;
}
