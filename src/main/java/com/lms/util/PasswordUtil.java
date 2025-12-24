package com.lms.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class để xử lý mã hóa và kiểm tra mật khẩu Sử dụng BCrypt để hash
 * password
 */
public class PasswordUtil {

    /**
     * Hash password bằng BCrypt
     *
     * @param password Mật khẩu gốc
     * @return Password đã được hash
     */
    public static String hashPassword(String password) {
        // BCrypt tự động tạo salt và hash password
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Kiểm tra password có khớp với hash không
     *
     * @param password Mật khẩu gốc cần kiểm tra
     * @param hash Password hash đã lưu trong database
     * @return true nếu khớp, false nếu không khớp
     */
    public static boolean checkPassword(String password, String hash) {
        try {
            return BCrypt.checkpw(password, hash);
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra password: " + e.getMessage());
            return false;
        }
    }
}
