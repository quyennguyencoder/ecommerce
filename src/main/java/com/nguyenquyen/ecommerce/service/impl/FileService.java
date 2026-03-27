package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService implements IFileService {
    @Value("${uploads.avatars}")
    private String avatarUploadPath;

    @Value("${uploads.products}")
    private String productUploadPath;

    @Override
    public String uploadAvatar(MultipartFile file) {
        return uploadFile(file, avatarUploadPath, "avatar");
    }

    @Override
    public String uploadProductImage(MultipartFile file) {
        return uploadFile(file, productUploadPath, "product");
    }

    @Override
    public Resource loadAvatarFile(String avatarFileName) {
        try {
            // Check if avatar file name is null or empty
            if (avatarFileName == null || avatarFileName.isEmpty()) {
                return loadDefaultAvatar();
            }

            // Try to load avatar file
            Path avatarPath = Paths.get(avatarUploadPath, avatarFileName);
            if (Files.exists(avatarPath)) {
                return new FileSystemResource(avatarPath);
            }

            // File not found, return default avatar
            return loadDefaultAvatar();
        } catch (Exception e) {
            return loadDefaultAvatar();
        }
    }

    @Override
    public Resource loadProductImage(String productImageFileName) {
        try {
            // Check if product image file name is null or empty
            if (productImageFileName == null || productImageFileName.isEmpty()) {
                return loadDefaultProductImage();
            }

            // Try to load product image file
            Path productImagePath = Paths.get(productUploadPath, productImageFileName);
            if (Files.exists(productImagePath)) {
                return new FileSystemResource(productImagePath);
            }

            // File not found, return default product image
            return loadDefaultProductImage();
        } catch (Exception e) {
            return loadDefaultProductImage();
        }
    }


    private Resource loadDefaultAvatar() {
        try {
            // Try to load default avatar from classpath or upload directory
            Path defaultAvatarPath = Paths.get(avatarUploadPath, "default-avatar.png");

            if (Files.exists(defaultAvatarPath)) {
                return new FileSystemResource(defaultAvatarPath);
            }

            // If no default avatar file exists, try from classpath
            return new org.springframework.core.io.ClassPathResource("static/images/default-avatar.png");
        } catch (Exception e) {
            throw new RuntimeException("Default avatar not found: " + e.getMessage(), e);
        }
    }

    private Resource loadDefaultProductImage() {
        try {
            // Try to load default product image from classpath or upload directory
            Path defaultProductImagePath = Paths.get(productUploadPath, "default-product.png");

            if (Files.exists(defaultProductImagePath)) {
                return new FileSystemResource(defaultProductImagePath);
            }

            // If no default product image file exists, try from classpath
            return new org.springframework.core.io.ClassPathResource("static/images/default-product.png");
        } catch (Exception e) {
            throw new RuntimeException("Default product image not found: " + e.getMessage(), e);
        }
    }

    private String uploadFile(MultipartFile file, String uploadPath, String fileType) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        try {
            // Create directory if not exists
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Generate unique file name
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFileName = fileType + "_" + UUID.randomUUID() + "." + fileExtension;

            // Save file
            Path filePath = uploadDir.resolve(uniqueFileName);
            Files.write(filePath, file.getBytes());

            return uniqueFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "file";
        }
        int lastDot = filename.lastIndexOf(".");
        if (lastDot > 0) {
            return filename.substring(lastDot + 1).toLowerCase();
        }
        return "file";
    }
}
