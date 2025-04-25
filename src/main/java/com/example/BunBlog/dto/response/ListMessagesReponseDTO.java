package com.example.BunBlog.dto.response;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListMessagesReponseDTO {
    private List<MessageReponseDTO> messageReponseDTO;
}
