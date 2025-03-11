package com.tech.pierandrei.Conversio.core.configs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.StatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;

@Converter(autoApply = true)
public class HashMapJsonConverter implements AttributeConverter<HashMap<String, StatusEnum>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(HashMap<String, StatusEnum> map) {
        try {
            return objectMapper.writeValueAsString(map); // Converte para JSON
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter HashMap para JSON", e);
        }
    }

    @Override
    public HashMap<String, StatusEnum> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, StatusEnum.class));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter JSON para HashMap", e);
        }
    }
}
