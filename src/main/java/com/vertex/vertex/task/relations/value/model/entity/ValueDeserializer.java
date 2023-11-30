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

@Component
public class ValueDeserializer extends StdDeserializer<Value> {

    private final PropertyService propertyService;

    protected ValueDeserializer(PropertyService propertyService) {
        super(Value.class);
        this.propertyService = propertyService;
    }

    @Override
    public Value deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        Property property = jsonParser.getCodec().treeToValue(node.get("property"), Property.class);

        property = propertyService.findById(property.getId());

        Value valueKind = property.getKind().getValue();
        valueKind.setProperty(property);
        if(node.get("value") != null) {
            JsonNode nodeV = node.get("value");
            System.out.println(nodeV.get("id"));
            if(nodeV.get("id") != null){
                valueKind.setId(nodeV.get("id").asLong());
            }

            if(nodeV.get("value") != null) {
                if(property.getKind() == PropertyKind.LIST ||
                property.getKind() == PropertyKind.STATUS){
                    String value = nodeV.get("value").asText();
                    Long idList = Long.parseLong(value);
                    for(PropertyList propertyList : property.getPropertyLists()){
                        if(idList.equals(propertyList.getId())){
                            valueKind.setValue(propertyList);
                        }
                    }
                }else {
                    String value = nodeV.get("value").asText();
                    valueKind.setValue(value);
                }
            }


        }
        System.out.println(valueKind);
        return valueKind;
    }
}
