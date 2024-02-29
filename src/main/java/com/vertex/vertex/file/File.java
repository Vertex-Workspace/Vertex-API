package com.vertex.vertex.file;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] file;

    public File(MultipartFile file)
            throws IOException {
        this.name = file.getOriginalFilename();
        this.type = file.getContentType();
        this.file = file.getBytes();
    }

}
