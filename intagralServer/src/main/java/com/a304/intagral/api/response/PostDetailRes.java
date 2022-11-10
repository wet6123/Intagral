package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Schema(description = "게시글 상세 정보 Response")
@Getter
@Setter
public class PostDetailRes extends BaseResponseBody {
    @Schema(name = "이미지 경로", example = "https://intagral-bucket-amazon.com")
    String imgPath;
    @Schema(name = "글에 달린 태그들")
    List<String> tags;
    @Schema(name = "게시글 좋아요 수", example = "18")
    Long likCount;
    @Schema(name = "팔로우 여부", example = "false")
    boolean isLike;
    @Schema(name = "작성자", example = "goodman")
    String writer;
    @Schema(name = "작성자이미지", example = "https://intagral-bucket-amazon.com")
    String writerImgPath;
    @Schema(name = "작성자 팔로우 여부", example = "true")
    @JsonProperty(value = "isFollow")
    boolean isFollow;
    public static  PostDetailRes of(int statusCode, String message, String imgPath, List<String> tags, Long likCount, boolean isLike, String writer, String writerImgPath, boolean isFollow){
        PostDetailRes res = new PostDetailRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setImgPath(imgPath);
        res.setTags(tags);
        res.setLikCount(likCount);
        res.setLike(isLike);
        res.setWriter(writer);
        res.setWriterImgPath(writerImgPath);
        res.setFollow(isFollow);
        return res;
    }
}
