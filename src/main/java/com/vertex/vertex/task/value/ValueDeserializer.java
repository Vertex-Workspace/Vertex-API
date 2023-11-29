package com.vertex.vertex.task.value;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vertex.vertex.task.value.model.entity.Value;
import com.vertex.vertex.task.value.model.entity.ValueText;

import java.io.IOException;
import java.sql.Date;

public class ValueDeserializer extends StdDeserializer<Value> {

    protected ValueDeserializer(Class<?> vc) {
        super(Value.class);
    }

    @Override
    public Value deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.get("value").asText();
        Long id = node.get("id").asLong();

        return new ValueText(id, value);

    }
}
