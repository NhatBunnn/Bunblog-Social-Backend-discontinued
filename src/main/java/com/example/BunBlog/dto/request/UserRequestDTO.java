package com.example.BunBlog.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    @NotBlank(message = "Tên không được trống")
    private String username;

    @NotBlank(message = "Email không được trống")
    @Email(message = "Phải điền đúng kiểu email")
    private String email;

    @NotBlank(message = "Mật khẩu không được trống")
    private String password;

    private String address;

    private MultipartFile avatar;

    

}
