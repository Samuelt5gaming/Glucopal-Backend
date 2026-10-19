package com.diabetesapp.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MediaService {

    private static final Set<String> FOLDERS = Set.of("meals", "avatars");
    private static final Set<String> TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_BYTES = 5L * 1024 * 1024;

    private final Cloudinary cloudinary;

    @Value("${cloudinary.cloud-name:}")
    private String cloudName;

    public Map<String, String> uploadImage(MultipartFile file, String folder, String firebaseUid) {
        if (cloudName.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Media storage is not configured");
        }
        if (!FOLDERS.contains(folder)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown folder");
        }
        if (file.isEmpty() || file.getSize() > MAX_BYTES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image must be between 1 byte and 5 MB");
        }
        if (file.getContentType() == null || !TYPES.contains(file.getContentType())) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only JPEG, PNG or WebP images are allowed");
        }
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "glucopal/" + folder + "/" + firebaseUid,
                    "resource_type", "image"));
            return Map.of(
                    "url", String.valueOf(result.get("secure_url")),
                    "publicId", String.valueOf(result.get("public_id")));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Image upload failed");
        }
    }
}
