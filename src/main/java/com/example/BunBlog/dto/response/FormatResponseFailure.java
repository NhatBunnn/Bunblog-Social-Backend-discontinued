package com.example.BunBlog.dto.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class FormatResponseFailure {
    private int statusCode;
    private String error;
    private String message;
    private Map<String, List<String>> data;

}