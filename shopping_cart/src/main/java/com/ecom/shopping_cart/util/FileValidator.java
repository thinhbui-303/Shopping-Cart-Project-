package com.ecom.shopping_cart.util;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileValidator {

    // Extension được phép
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
        "jpg", "jpeg", "png", "webp", "gif"
    );

    // MIME type được phép
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
        "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    // Giới hạn kích thước 5MB
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * Validate file upload - kiểm tra 3 lớp
     * @return null nếu hợp lệ, error message nếu không hợp lệ
     */
    public static String validate(MultipartFile file) throws IOException {

        // Không có file → bỏ qua (file là optional ở nhiều chỗ)
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Lớp 1: Kiểm tra extension
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            return "File must have a valid extension!";
        }

        String extension = originalFileName
                .substring(originalFileName.lastIndexOf(".") + 1)
                .toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            return "Only image files are allowed (jpg, jpeg, png, webp, gif)!";
        }

        // Lớp 2: Kiểm tra kích thước
        if (file.getSize() > MAX_FILE_SIZE) {
            return "File size must not exceed 5MB!";
        }

        // Lớp 3: Kiểm tra magic bytes (nội dung thật của file)
        Tika tika = new Tika();
        String detectedMimeType = tika.detect(file.getInputStream());

        if (!ALLOWED_MIME_TYPES.contains(detectedMimeType)) {
            return "File content is not a valid image!";
        }

        return null; 
    }
}