package com.vertex.vertex.task.value.model.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ValueFile extends Value {

    private File value;

}
