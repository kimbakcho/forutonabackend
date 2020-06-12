package com.wing.forutona.CustomUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class JsonToMultoSortsConverter implements Converter<String, FSorts> {
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public FSorts convert(String s) {
        try {
            return jsonMapper.readValue(s, FSorts.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
