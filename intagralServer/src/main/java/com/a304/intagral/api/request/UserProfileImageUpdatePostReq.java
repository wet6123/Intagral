package com.a304.intagral.api.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserProfileImageUpdatePostReq {

    MultipartFile data;
}
