package com.nguyenquyen.ecommerce.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    String uploadFile(MultipartFile file);
    Resource loadFile(String fileName);
}
