package com.example.BunBlog.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.BunBlog.config.consoleSyntax;
import com.example.BunBlog.dto.response.FormatResponseFailure;
import com.example.BunBlog.dto.response.FormatResponseSuccess;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class FormatResponse implements ResponseBodyAdvice<Object>{

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {

        if (body instanceof String){
            return body;
        }

        int status = HttpStatus.OK.value();
        if (response instanceof ServletServerHttpResponse) {
            status = ((ServletServerHttpResponse) response).getServletResponse().getStatus();
        }

        if(status >= 400){
            return body;
        }

        FormatResponseSuccess formatResponseSuccess = new FormatResponseSuccess();
        formatResponseSuccess.setStatusCode(status);
        formatResponseSuccess.setMessage("(Tạm) Thành công");
        formatResponseSuccess.setData(body);

        return formatResponseSuccess;
    }
    
}
