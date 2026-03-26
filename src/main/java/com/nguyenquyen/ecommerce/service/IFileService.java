package com.nguyenquyen.ecommerce.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    String uploadAvatar(MultipartFile file);
    String uploadProductImage(MultipartFile file);
    Resource loadAvatarFile(String avatarFileName);
}
