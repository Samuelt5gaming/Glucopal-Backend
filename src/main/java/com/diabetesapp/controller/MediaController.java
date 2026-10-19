package com.diabetesapp.controller;

import com.diabetesapp.security.FirebasePrincipal;
import com.diabetesapp.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/me/uploads")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    /** multipart/form-data: file=<image>, folder=meals|avatars. Returns {url, publicId}. */
    @PostMapping
    public ResponseEntity<Map<String, String>> upload(
            @AuthenticationPrincipal FirebasePrincipal me,
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "meals") String folder) {
        return ResponseEntity.ok(mediaService.uploadImage(file, folder, me.uid()));
    }
}
