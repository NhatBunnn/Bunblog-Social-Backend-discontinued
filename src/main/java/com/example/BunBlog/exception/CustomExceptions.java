package com.example.BunBlog.exception;

public class CustomExceptions {
    // Exception khi không tìm thấy người dùng
    public static class UsernameNotFoundException extends RuntimeException {
        public UsernameNotFoundException(String message) {
            super(message);
        }
    }

    // Exception khi thông tin đăng nhập sai
    public static class BadCredentialsException extends RuntimeException {
        public BadCredentialsException(String message) {
            super(message);
        }
    }

    // Exception khi ID không hợp lệ
    public static class IdInvalidException extends Exception {
        public IdInvalidException(String message) {
            super(message);
        }
    }

    // Exception khi không có quyền truy cập
    public static class UnauthorizedAccessException extends RuntimeException {
        public UnauthorizedAccessException(String message) {
            super(message);
        }
    }
}
