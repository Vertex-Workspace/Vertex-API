package com.vertex.vertex.task_property.value.service;


import com.vertex.vertex.task_property.value.model.entity.Value;
import com.vertex.vertex.task_property.value.repository.ValueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ValueService {
    private final ValueRepository valueRepository;

    public Value save(Value task) {
        return valueRepository.save(task);
    }

    public Collection<Value> findAll() {
        return valueRepository.findAll();
    }

    public Value findById(Long id) {
        return valueRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        valueRepository.deleteById(id);
    }
}
