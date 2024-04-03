package com.vertex.vertex.task.relations.value.service;

import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.value.model.entity.ValueDate;
import com.vertex.vertex.task.relations.value.repository.ValueRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public void setTaskDefaultValues(Task task, List<Property> properties) {
        List<Value> values = new ArrayList<>();

        for (Property property : properties) {
            Value currentValue = property.getKind().getValue();
            currentValue.setProperty(property);
            currentValue.setTask(task);
            values.add(currentValue);

            if (property.getKind() == PropertyKind.STATUS) {
                //Get the first element, how the three are fixed, it always will be TO DO "Não Iniciado"
                currentValue.setValue(property.getPropertyLists().get(0));
            }
            if (property.getKind() == PropertyKind.DATE) {
                ((ValueDate) currentValue).setValue();
            }
            if (property.getKind() == PropertyKind.TEXT) {
                currentValue.setValue(property.getDefaultValue());
            }
        }

        task.setValues(values);
    }

}
