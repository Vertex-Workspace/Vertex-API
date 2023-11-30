package com.vertex.vertex.task.value.model.entity;

import com.vertex.vertex.task.model.entity.TaskProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Value{



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private TaskProperty taskProperty;
    public abstract Object getValue();
    public abstract void setValue(Object object);


}
