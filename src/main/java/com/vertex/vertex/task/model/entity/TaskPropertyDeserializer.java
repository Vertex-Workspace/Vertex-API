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

    private final PropertyService propertyService;

    protected TaskPropertyDeserializer(PropertyService propertyService) {
        super(TaskProperty.class);
        this.propertyService = propertyService;
    }

    @Override
    public TaskProperty deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        System.out.println("deserializer");
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        TaskProperty taskProperty = new TaskProperty();

        Property property = jsonParser.getCodec().treeToValue(node.get("property"), Property.class);

        property = propertyService.findById(property.getId());

        if(node.get("value") != null) {
            JsonNode nodeV = node.get("value");

            Value valueKind = property.getKind().getValue();

            if(nodeV.get("id") != null){
                Long id = nodeV.get("id").asLong();
                valueKind.setId(id);
            }

//        Long idTP = node.get("id").asLong();
//        taskProperty.setId(idTP);

            if(nodeV.get("value") != null) {
                String value = nodeV.get("value").asText();
                valueKind.setValue(value);
            }

            taskProperty.setValue(valueKind);
        }
        taskProperty.setProperty(property);
        return taskProperty;
    }
}
