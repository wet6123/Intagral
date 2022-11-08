package com.a304.intagral.api.response;

import com.a304.intagral.common.response.BaseResponseBody;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDetailRes extends BaseResponseBody {

    String imgPath;
    List<String> tags;
    Long likCount;
    boolean isLike;
    String writer;
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
