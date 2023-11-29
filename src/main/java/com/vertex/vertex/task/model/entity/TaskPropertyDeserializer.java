package com.vertex.vertex.task.model.entity;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.value.model.entity.Value;
import com.vertex.vertex.task.value.model.entity.ValueText;
import com.vertex.vertex.task.value.service.ValueService;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TaskPropertyDeserializer extends StdDeserializer<TaskProperty> {

    private PropertyService propertyService;

    protected TaskPropertyDeserializer(PropertyService propertyService) {
        super(TaskProperty.class);
        this.propertyService = propertyService;
    }

    @Override
    public TaskProperty deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        TaskProperty taskProperty = new TaskProperty();
        JsonNode nodeV = node.get("value");
        Property property = jsonParser.getCodec().treeToValue(node.get("property"), Property.class);
        property = propertyService.findById(property.getId());
        Value valueKind = property.getKind().getValue();

        try {
            Long id = nodeV.get("id").asLong();
            Long idTP = node.get("id").asLong();
            valueKind.setId(id);
            taskProperty.setId(idTP);
        }catch(Exception ignore){

        }

        String value = nodeV.get("value").asText();
        System.out.println(value);
        System.out.println(valueKind);
        valueKind.setValue(value);

        System.out.println(valueKind);

        taskProperty.setProperty(property);
        taskProperty.setValue(valueKind);
        return taskProperty;
    }
}
