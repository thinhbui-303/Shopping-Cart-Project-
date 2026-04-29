package com.ecom.shopping_cart.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.shopping_cart.util.FileValidator;

@Service
public class FileStorageService {
    @Value("${app.upload.path}")
    private String uploadPath;

    public void saveFile(MultipartFile file, String subFolder) throws IOException
    {
        String validationError = FileValidator.validate(file);
                if (validationError != null) {
                    throw new IllegalArgumentException(validationError);
                }

        Path dir = Paths.get(uploadPath, subFolder);
        Files.createDirectories(dir);
       // Lấy filename an toàn - tránh Path Traversal
        String safeFileName = Paths.get(file.getOriginalFilename())
                .getFileName()
                .toString();

        Path destination = dir.resolve(safeFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

    }
}
