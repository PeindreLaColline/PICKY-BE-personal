package com.ureca.picky_be.base.implementation.content;

import com.ureca.picky_be.config.S3Config;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileManager {

    private final S3Config s3Config;
    private final S3Presigner s3Presigner;

    @PostConstruct
    private void init(){
        if(s3Config.getEnvironment().equals("local")){
            s3Config.getLocalFileDir();
        }
        else if(s3Config.getEnvironment().equals("aws")){
            s3Config.getProdFileDir();
        }
    }

    // [1] MultipartFile 리스트를 File로 변환하고 각각 업로드
    public String uploadProfile(MultipartFile multipartFile) throws IOException {
        File uploadFile = multipartFileToFile(multipartFile).orElseThrow(()-> new CustomException(ErrorCode.CONTENT_CONVERT_FAILED));
        return extractFilePath(uploadProfile(uploadFile));
    }

    // [1-1] MultipartFile을 File로 변환 및 로컬에 저장
    private Optional<File> multipartFileToFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            throw new CustomException(ErrorCode.CONTENT_TO_CONVERT_NOT_FOUND);
        }

        String storeFileName = createStoreFileName(multipartFile.getOriginalFilename());

        //변환된 파일이 저장될 경로와 파일 이름으로 File 객체 생성
        File file = new File(s3Config.getFileDir()  + "profile/" + storeFileName);

        try {
            //변환한 데이터를 File객체에 저장
            multipartFile.transferTo(file);
            return Optional.of(file);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.CONTENT_CONVERT_FAILED);
        }
    }

    // [1-2] S3로 파일 업로드 및 로컬 파일 삭제
    private String uploadProfile(File uploadFile) throws IOException {

        String fileName = "profile/" + UUID.randomUUID() + uploadFile.getName();
        String uploadProfileUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);
        return uploadProfileUrl;
    }

    // [1-2-1] S3에 업로드
    private String putS3(File uploadFile, String fileName) throws IOException {
        //S3에 업로드할 파일의 메타데이터 및 설정 정보
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Config.getBucket())
                .key(fileName)
                .build();
        if(!uploadFile.exists()){
            throw new CustomException(ErrorCode.CONTENT_TO_CONVERT_NOT_FOUND);
        }
        try {
            s3Config.s3Client().putObject(putObjectRequest, uploadFile.toPath()); //uploadFile.toPath(): 업로드할 로컬 파일의 경로
            return "https://" + s3Config.getBucket() + ".s3." + s3Config.getRegion() + ".amazonaws.com/" + fileName;
        } catch (S3Exception e) {
            throw new CustomException(ErrorCode.S3_UPLOAD_FAILED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    //로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile){
        try{
            targetFile.delete();
        } catch (Exception e){
            throw new CustomException(ErrorCode.LOCAL_CONTENT_TO_DELETE_NOT_FOUND);
        }
    }

    // UUID 생성
    private String createStoreFileName(String originalFilename){
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 확장자 추출
    private String extractExt(String originalFilename){
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    // URL에서 경로만 추출하는 메서드
    private String extractFilePath(String url) {
        try {
            return new URL(url).getPath().substring(1);
        } catch (MalformedURLException e) {
            throw new CustomException(ErrorCode.EXTRACT_RUL_FAILED);
        }
    }

    // 임시 접근 url 발급
    public String getPresignedUrl(String objectKey) {
        return generatePresignedUrl(objectKey);
    }

    public String generatePresignedUrl(String objectKey) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Config.getBucket())
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofDays(1))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
