package com.lms.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.servlet.ServletContext;

public class FileUtil {

    public static String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot + 1).toLowerCase() : "";
    }

    public static boolean isAllowedFileType(String fileName) {
        String ext = getFileExtension(fileName);
        return "pdf".equals(ext) || "doc".equals(ext) || "docx".equals(ext) || "txt".equals(ext);
    }

    public static String generateUniqueFileName(String originalName) {
        String ext = getFileExtension(originalName);
        return UUID.randomUUID().toString() + (ext.isEmpty() ? "" : "." + ext);
    }

    public static String getUploadDirectory(ServletContext context) {
        String uploadDir = context.getRealPath("/uploads/documents");
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return uploadDir;
    }

    public static boolean saveFile(InputStream inputStream, String fileName, String uploadDir) {
        try {
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu file: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            return file.delete();
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa file: " + e.getMessage());
            return false;
        }
    }

    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        }
        if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        }
        return String.format("%.2f MB", size / (1024.0 * 1024.0));
    }
}
