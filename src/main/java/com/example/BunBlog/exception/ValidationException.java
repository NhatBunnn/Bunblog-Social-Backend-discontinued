package com.example.BunBlog.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {
        private Map<String, List<String>> message = new HashMap<>();


    public ValidationException(Map<String, List<String>> message) {
        super("Validation failed");
        this.message = message;
    }

    public Map<String, List<String>> getErrors() {
        return message;
    }
}
