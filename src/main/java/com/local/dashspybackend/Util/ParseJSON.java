package com.local.dashspybackend.Util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Component
public class ParseJSON {
    @Autowired
    private ObjectMapper modelMapper;

    public <T> T parse(String input, Class<T> targetType) {
        try {
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
            };
            Map<String, Object> payload = modelMapper.readValue(input, typeRef);
            T k = modelMapper.convertValue(payload, targetType);
            return k;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // throw new Exception("Convert json to class instance error");
            return targetType.cast(new Object());
        }

    }
}
