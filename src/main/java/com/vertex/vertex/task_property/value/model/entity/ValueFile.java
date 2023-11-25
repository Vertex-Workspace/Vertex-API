package com.vertex.vertex.task_property.value.model.entity;

import com.vertex.vertex.task_property.model.entity.Value;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ValueFile extends Value{

    private File file;

}
