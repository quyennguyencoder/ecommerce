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

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Kiểm tra định dạng file (chỉ cho phép JPEG và PNG)
        String originalFilename = file.getOriginalFilename();
        if (!isValidFileType(originalFilename)) {
            throw new IllegalArgumentException("File type not allowed. Only JPEG and PNG are supported");
        }

        try {
            // Tạo thư mục nếu chưa tồn tại
            Path uploadDirPath = Paths.get(uploadDir);
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }

            // Tạo tên file duy nhất
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFileName = UUID.randomUUID() + "." + fileExtension;

            // Lưu file
            Path filePath = uploadDirPath.resolve(uniqueFileName);
            Files.write(filePath, file.getBytes());

            return baseUrl + "/" + "api/v1/files/" + uniqueFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public Resource loadFile(String fileName) {
        try {
            // Xây dựng đường dẫn file
            Path filePath = Paths.get(uploadDir, fileName).normalize();
            java.io.File file = filePath.toFile();

            // Kiểm tra file tồn tại
            if (!file.exists() || !file.isFile()) {
                throw new RuntimeException("File không tồn tại: " + fileName);
            }

            // Kiểm tra path traversal attack
            String uploadDirAbsolute = new java.io.File(uploadDir).getCanonicalPath();
            String fileAbsolute = file.getCanonicalPath();
            if (!fileAbsolute.startsWith(uploadDirAbsolute)) {
                throw new RuntimeException("Access denied: " + fileName);
            }

            // Trả về Resource
            return new FileSystemResource(file);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load file: " + e.getMessage(), e);
        }
    }


    private boolean isValidFileType(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }
        String lowerFilename = filename.toLowerCase();
        return lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg") || lowerFilename.endsWith(".png");
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
