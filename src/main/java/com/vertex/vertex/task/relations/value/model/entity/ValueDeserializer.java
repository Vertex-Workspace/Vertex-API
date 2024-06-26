package com.vertex.vertex.task.relations.value.model.entity;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.property.service.PropertyService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class ValueDeserializer extends StdDeserializer<Value> {

    private final PropertyService propertyService;

    protected ValueDeserializer(PropertyService propertyService) {
        super(Value.class);
        this.propertyService = propertyService;
    }

    @Override
    public Value deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        Property property = jsonParser.getCodec().treeToValue(node.get("property"), Property.class);

        property = propertyService.findById(property.getId());

        //Inversão de Dependência
        Value valueKind = property.getKind().getValue();
        valueKind.setProperty(property);
        if (node.get("value") != null) {
            JsonNode nodeV = node.get("value");
            if (nodeV.get("id") != null) {
                valueKind.setId(nodeV.get("id").asLong());
            }

            if (nodeV.get("value") != null) {
                String value = nodeV.get("value").asText();
                if (property.getKind() == PropertyKind.LIST ||
                        property.getKind() == PropertyKind.STATUS) {
                    Long idList = Long.parseLong(value);
                    for (PropertyList propertyList : property.getPropertyLists()) {
                        if (idList.equals(propertyList.getId())) {
                            valueKind.setValue(propertyList);
                        }
                    }
                } else {
                    valueKind.setValue(value);
                }
            }


        }
        return valueKind;
    }
}
