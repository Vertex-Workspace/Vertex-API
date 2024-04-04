package com.vertex.vertex.task.relations.value.service;

import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.value.repository.ValueRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class ValueService {

    private ValueRepository valueRepository;

    public void delete(Value value){
        valueRepository.delete(value);
    }
    public void deleteById(Long id){
        valueRepository.deleteById(id);
    }
    public void save(Value value){
        valueRepository.save(value);
    }
}
