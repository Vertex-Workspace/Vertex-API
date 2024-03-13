package com.vertex.vertex.chat.relations.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageFileDTO {
    Message message;
    MultipartFile multipartFile;
}
