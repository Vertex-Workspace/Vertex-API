package com.vertex.vertex.task.value.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.Blob;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ValueFile extends Value {

    @Lob
    @Column(length = 999999999)
    private byte[] value;

    @Override
    public void setValue(Object object) {
        String objectM = (String) object;
        this.value = objectM.getBytes();
    }
}
