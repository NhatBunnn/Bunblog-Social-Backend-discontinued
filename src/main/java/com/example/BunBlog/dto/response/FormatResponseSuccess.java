package com.example.BunBlog.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormatResponseSuccess {
    private int statusCode;
    // message có thể là String hoặc arrayList
    private Object message;
    private Object data;
}
