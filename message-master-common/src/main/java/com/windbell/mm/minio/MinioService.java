package com.windbell.mm.minio;

import com.windbell.mm.exception.MessageException;
import com.windbell.mm.utils.MyUUID;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    /**
     * 上传图片到 MinIO
     */
    public void uploadFile(MultipartFile file) {
        try {
            // 检查存储桶是否存在，不存在则创建
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket("message-master").build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("message-master").build());
            }
            // 生成唯一文件名
            String filename = MyUUID.create(0, 12) + "-" + file.getOriginalFilename();
            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket("message-master").object(filename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType()).build());
            // 返回文件访问URL
            //return minioProperties.getEndpoint() + "/" + minioProperties.getBucketName() + "/" + filename;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MessageException("上传失败：" + e.getMessage());
        }
    }

    public void uploadFile(MultipartFile file,String account) {
        try {
            // 检查存储桶是否存在，不存在则创建
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket("message-master").build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("message-master").build());
            }
            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket("message-master").object(account)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType()).build());
            // 返回文件访问URL
            //return minioProperties.getEndpoint() + "/" + minioProperties.getBucketName() + "/" + filename;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MessageException("上传失败：" + e.getMessage());
        }
    }

    /**
     * 下载图片
     */
    public InputStream downloadFile(String filename) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket("message-master")
                    .object(filename).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MessageException("下载失败：" + e.getMessage());
        }
    }

    /**
     * 删除图片
     */
    public void deleteFile(String filename) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket("message-master")
                    .object(filename).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MessageException("删除失败：" + e.getMessage());
        }
    }
}
