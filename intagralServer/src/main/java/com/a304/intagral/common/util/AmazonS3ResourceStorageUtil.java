package com.a304.intagral.common.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
@RequiredArgsConstructor
public class AmazonS3ResourceStorageUtil {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    public String store(String fullPath, MultipartFile multipartFile) {
        // 디렉토리가 없으면 생성
        File directory = new File(MultipartUtil.getLocalHomeDirectory(), MultipartUtil.BASE_DIR);
        if (!directory.exists()) {
            try{
                directory.mkdir();
            } catch(Exception e){
                e.getStackTrace();
            }
        }
        File file = new File(MultipartUtil.getLocalHomeDirectory(), fullPath);
        try {
            multipartFile.transferTo(file);
            amazonS3Client.putObject(new PutObjectRequest(bucket, fullPath, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (file.exists()) {
                file.delete();
            }
        }

        return amazonS3Client.getResourceUrl(bucket, fullPath);
    }
}
