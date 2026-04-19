package com.ecom.shopping_cart.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    @Value("${app.upload.path}")
    private String uploadPath;

    public void saveFile(MultipartFile file, String subFolder) throws IOException
    {
        Path dir = Paths.get(uploadPath, subFolder);
        Files.createDirectories(dir);
        Path destination = dir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

    }
}
