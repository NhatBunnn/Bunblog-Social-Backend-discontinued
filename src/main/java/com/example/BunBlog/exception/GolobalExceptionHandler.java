package com.example.BunBlog.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.BunBlog.config.consoleSyntax;
import com.example.BunBlog.dto.response.FormatResponseFailure;

@ControllerAdvice
public class GolobalExceptionHandler {
    // Xử lý exception UsernameNotFoundException
    @ExceptionHandler(CustomExceptions.UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(CustomExceptions.UsernameNotFoundException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("Email", List.of("Tên hoặc mật khẩu không khớp"));


        FormatResponseFailure format = new FormatResponseFailure();
        format.setStatusCode(404);
        format.setData(errors);
        format.setError(ex.getMessage());

        return new ResponseEntity<>(format, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(CustomExceptions.BadCredentialsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(CustomExceptions.IdInvalidException.class)
    public ResponseEntity<String> handleIdInvalidException(CustomExceptions.IdInvalidException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomExceptions.UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccessException(CustomExceptions.UnauthorizedAccessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {

        FormatResponseFailure format = new FormatResponseFailure();
        format.setStatusCode(404);
        format.setMessage(ex.getMessage());
        format.setError(null);

        return new ResponseEntity<>(format, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {

        FormatResponseFailure format = new FormatResponseFailure();
        format.setStatusCode(404);
        format.setMessage(ex.getMessage());
        format.setError(null);

        return new ResponseEntity<>(format, HttpStatus.NOT_FOUND);
    }

   // @ExceptionHandler(MethodArgumentNotValidException.class) giúp bắt lỗi validation của @Valid trong Spring Boot.
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<Object> validationError(MethodArgumentNotValidException ex) {
       BindingResult result = ex.getBindingResult();
       Map<String, List<String>> errors = new HashMap<>();

        result.getFieldErrors().forEach(error -> {
            errors.computeIfAbsent(error.getField(), k -> new java.util.ArrayList<>()).add(error.getDefaultMessage());
        });
        FormatResponseFailure format = new FormatResponseFailure();
        format.setStatusCode(HttpStatus.BAD_REQUEST.value());
        format.setError("Validation failed");
        format.setData(errors);
   
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(format);
   }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        FormatResponseFailure format = new FormatResponseFailure();
        format.setStatusCode(HttpStatus.BAD_REQUEST.value());
        format.setMessage(ex.getMessage());
        format.setData(ex.getErrors());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(format);
    }

}
