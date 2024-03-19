package com.vertex.vertex.file.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.task.relations.note.model.entity.Note;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    private String codeAWS;

    @Lob
    @Column(columnDefinition = "BLOB")
    @ToString.Exclude
    private byte[] file;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Note note;

    public File(MultipartFile file, Note note)
            throws IOException {
        this.name = file.getOriginalFilename();
        this.type = file.getContentType();
        this.file = file.getBytes();
        this.note = note;
    }

    public File(MultipartFile file) throws IOException {
        this.file = file.getBytes();
    }

    public File(String name, String codeAWS) {
        this.name = name;
        this.codeAWS = codeAWS;
    }
}
