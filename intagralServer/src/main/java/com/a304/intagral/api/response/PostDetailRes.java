package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel("게시글 상세 정보 Response")
@Getter
@Setter
public class PostDetailRes extends BaseResponseBody {
    @ApiModelProperty(name = "이미지 경로", example = "https://intagral-bucket-amazon.com")
    String imgPath;
    @ApiModelProperty(name = "글에 달린 태그들")
    List<String> tags;
    @ApiModelProperty(name = "게시글 좋아요 수", example = "18")
    Long likCount;
    @ApiModelProperty(name = "팔로우 여부", example = "false")
    boolean isLike;
    @ApiModelProperty(name = "작성자", example = "goodman")
    String writer;
    @ApiModelProperty(name = "작성자이미지", example = "https://intagral-bucket-amazon.com")
    String writerImgPath;

    public static  PostDetailRes of(int statusCode, String message, String imgPath, List<String> tags, Long likCount, boolean isLike, String writer, String writerImgPath){
        PostDetailRes res = new PostDetailRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setImgPath(imgPath);
        res.setTags(tags);
        res.setLikCount(likCount);
        res.setLike(isLike);
        res.setWriter(writer);
        res.setWriterImgPath(writerImgPath);
        return res;
    }
}
