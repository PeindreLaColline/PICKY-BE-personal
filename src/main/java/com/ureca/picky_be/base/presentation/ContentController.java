package com.ureca.picky_be.base.presentation;

import com.ureca.picky_be.base.implementation.content.ImageManager;
import com.ureca.picky_be.base.implementation.content.ProfileManager;
import com.ureca.picky_be.base.implementation.content.VideoManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/content")
public class ContentController {

    private final ImageManager imageManager;
    private final VideoManager videoManager;
    private final ProfileManager profileManager;

    @PostMapping("/image")
    public String uploadImage(@RequestParam("data") List<MultipartFile> multipartFiles) throws IOException {
        return imageManager.uploadImages(multipartFiles).toString();
    }

    @GetMapping("/image")
    public String getImage(@RequestParam String objectKey){
        return imageManager.getPresignedUrl(objectKey);
    }

    @PostMapping("/video")
    public String uploadVideo(@RequestParam("data") List<MultipartFile> multipartFiles) throws IOException {
        return videoManager.uploadVideo(multipartFiles).toString();
    }

    @GetMapping("/video")
    public String getVideo(@RequestParam String objectKey){
        return videoManager.getPresignedUrl(objectKey);
    }

    @PostMapping("/profile")
    public String uploadProfile(@RequestParam("data") MultipartFile multipartFiles) throws IOException {
        return profileManager.uploadProfile(multipartFiles);
    }

    @GetMapping("/profile")
    public String getProfile(@RequestParam String objectKey){
        return profileManager.getPresignedUrl(objectKey);
    }

}
