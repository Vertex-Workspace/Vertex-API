package com.vertex.vertex.file.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.task.model.entity.Task;
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
    @Column(columnDefinition = "LONGBLOB")
    @ToString.Exclude
    private byte[] file;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Note note;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Task task;


    public File(MultipartFile file, FileSupporter item)
            throws IOException {
        this.setValues(file);
        if (item instanceof Note) this.note = (Note) item;
        else this.task = (Task) item;
    }

    public File(MultipartFile file) throws IOException {
        this.setValues(file);
    }
    private void setValues(MultipartFile file) throws IOException {
        this.name = file.getOriginalFilename();
        this.type = file.getContentType();
        this.file = file.getBytes();
    }
}