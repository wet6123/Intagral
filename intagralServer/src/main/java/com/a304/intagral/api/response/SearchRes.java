package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.a304.intagral.db.dto.SearchHashtagDto;
import com.a304.intagral.db.dto.SearchUserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchRes extends BaseResponseBody {
    @JsonProperty(value = "users")
    List<SearchUserDto> users;
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
