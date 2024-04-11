package com.vertex.vertex.task.relations.value.service;

import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.value.model.DTOs.EditValueDTO;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.value.model.entity.ValueDate;
import com.vertex.vertex.task.relations.value.repository.ValueRepository;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public Task updateTaskValues(Task task, EditValueDTO editValueDTO,
                                 Property property, UserTeam userTeam) {

        for (Value value : task.getValues()) {
            if (value.getId().equals(editValueDTO.getValue().getId())) {
                Value currentValue = property.getKind().getValue();
                currentValue.setId(editValueDTO.getValue().getId());
                currentValue.setTask(task);
                currentValue.setProperty(property);

                if (validPropertyValueModification
                        (task, userTeam, property, value, editValueDTO)) {

                    int i = (task.getValues().indexOf(getValueFromProperty(property, task)));
                    currentValue.setValue(editValueDTO.getValue().getValue());
                    task.getValues().set(i, currentValue);
                    break;
                }
            }
        }
        return task;
    }

    public boolean validPropertyValueModification(
            Task task, UserTeam userTeam, Property property,
            Value value, EditValueDTO editValueDTO) {

        if (property.getKind() == PropertyKind.STATUS) {
            if (!userTeam.equals(task.getCreator()) && task.isRevisable()) {

                // Validates another -> done
                PropertyList propertyList = (PropertyList) editValueDTO.getValue().getValue();
                if (propertyList.getPropertyListKind().equals(PropertyListKind.DONE)) {
                    throw new RuntimeException("Não é possível definir como concluído, " +
                            "pois a tarefa deve passar por uma revisão do criador!");
                }

                // Validates done -> another
                PropertyList propertyListCurrent = (PropertyList) value.getValue();
                if (propertyListCurrent.getPropertyListKind().equals(PropertyListKind.DONE)) {
                    throw new RuntimeException("Apenas o criador da tarefa pode remover dos concluídos!");
                }
            }
        }
        return true;
    }

    public Value getValueFromProperty(Property property, Task task) {
        return task.getValues()
                .stream()
                .filter(v -> Objects.equals(property.getId(), v.getProperty().getId()))
                .findFirst()
                .get();

    }

}
