package org.ff4j.json.utils;

/*-
 * #%L
 * ff4j-json
 * %%
 * Copyright (C) 2013 - 2019 FF4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.ff4j.core.test.AssertUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * Setup Json Serialization.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class FF4jObjectMapper {
    
    public static final String DATEPATTERN    = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final SimpleDateFormat  SDF = new SimpleDateFormat(DATEPATTERN);
    public static final DateTimeFormatter DF  = DateTimeFormatter.ofPattern(DATEPATTERN);
    
    /** Singleton Pattern. */
    private static FF4jObjectMapper _instance;
    
    /** Jackson Mapper. */
    private ObjectMapper mapper;
    
    /** Store readers. */
    private Map<String, ObjectReader> mapOfReaders = new HashMap<>();
    
    /** Singleton Pattern. */
    public static final synchronized FF4jObjectMapper getInstance() {
        if (null == _instance) {
            _instance = new FF4jObjectMapper();
        }
        return _instance;
    }
    
    public static ObjectMapper getMapper() {
        return getInstance().mapper;
    }
    
    public static ObjectMapper getMapperReaders() {
        return getInstance().mapper;
    }
    
    public static final String toJson(Object o) {
        if (null == o) return null;
        try {
            return getMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot serialize object as json", e);
        }
    }
    
    public static final <T> T fromJson(String json, Class<T> myClass) {
        if (null == json) return null;
        AssertUtils.assertNotNull(myClass);
        _instance = getInstance();
        if (!_instance.mapOfReaders.containsKey(myClass.getName())) {
            _instance.mapOfReaders.put(myClass.getName(), FF4jObjectMapper.getMapper().readerFor(myClass));
        }
        try {
            return _instance.mapOfReaders.get(myClass.getName()).readValue(json);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot marshall object from json", e);
        }
    }
    
    /**
     * Default Constructor.
     */
    private FF4jObjectMapper() {
        mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        // Global
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        
        // Serialization
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        // Deserialization
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.setDateFormat(SDF);
    } 
}

