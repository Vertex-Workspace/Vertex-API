package com.vertex.vertex.file.model;

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

    private String fileName;
    private String awsKey;
    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] image;

    public File(MultipartFile file)
            throws IOException {

    }

    public File(String fileName, String awsKey) {
        this.fileName = fileName;
        this.awsKey = awsKey;
    }
}
