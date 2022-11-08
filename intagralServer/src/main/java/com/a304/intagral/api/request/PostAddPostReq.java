package com.a304.intagral.api.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostAddPostReq {
    MultipartFile  image;
    List<String> hashtags;
}
