package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.SearchHashtagDto;
import com.a304.intagral.db.dto.SearchUserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel("검색 결과 Response")

@Getter
@Setter
public class SearchRes extends BaseResponseBody {
    @ApiModelProperty(name = "유저", example = "[goodman, tiger, temp_DS45j]")
    @JsonProperty(value = "users")
    List<SearchUserDto> users;
    @ApiModelProperty(name = "해시태그", example = "[chair, 허먼밀러, 낡은 책상]")
    @JsonProperty(value = "hashtags")
    List<SearchHashtagDto> hashtags;

    public static SearchRes of(int statusCode, String message, List<SearchUserDto> users, List<SearchHashtagDto> hashtags) {
        SearchRes res = new SearchRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setUsers(users);
        res.setHashtags(hashtags);

        return res;
    }
}
