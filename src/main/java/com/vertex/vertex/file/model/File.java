package com.vertex.vertex.file.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

}
