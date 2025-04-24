package com.eighttoten.member.service;

import com.eighttoten.exception.BadRequestException;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.InternalException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MultipartFileStorageService {
    @Value("${app.file.image-path}")
    private String imageDirectoryPath;

    public String saveImageFile(MultipartFile multipartFile) throws IOException {
        validateImageFileType(multipartFile.getContentType());
        String originalFilename = multipartFile.getOriginalFilename();

        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new BadRequestException(ExceptionCode.REQUIRE_FILE_NAME);
        }

        String filePath = imageDirectoryPath + generateRandomFileName(originalFilename);

        File file = new File(filePath);

        if (!file.exists()){
            createDirectoryIfNotExists(imageDirectoryPath);
            multipartFile.transferTo(file);
        }

        return filePath;
    }

    public void deleteFile(String filePath) {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new InternalException(ExceptionCode.FAILED_FILE_DELETE);
            }
        }
    }

    private void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        directory.mkdirs();
    }

    private String generateRandomFileName(String originalFileName) {
        int randomInt = ThreadLocalRandom.current().nextInt(1_000_000);
        return UUID.randomUUID() + "_" + randomInt + extractFileExtension(originalFileName);
    }

    private String extractFileExtension(String fileName) {
        String lowerCase = fileName.toLowerCase();
        return lowerCase.substring(lowerCase.lastIndexOf("."));
    }

    private void validateImageFileType(String contentType) {
        if(contentType == null) {
            throw new BadRequestException(ExceptionCode.REQUIRE_CONTENT_TYPE);
        }

        if (!contentType.startsWith("image/")) {
            throw new BadRequestException(ExceptionCode.REQUIRE_IMAGE_FILE);
        }
    }
}