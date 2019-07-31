package org.ff4j.utils;

import java.util.Arrays;
import java.util.Collection;

/*
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2015 Ff4J
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

import java.util.Map;
import java.util.stream.Collectors;

import org.ff4j.FF4jRepository;
import org.ff4j.cache.CacheProxy;

/**
 * Custom implementation of serialization : faster + no jackson dependency
 * 
 * @author Cedrick Lunven (@clunven)
 */
public class JsonUtils {
    
    public static final String COMMA     = "#COM#";
    public static final String SEMICOLON = "#SEMCOL#";
    public static final String QUOTE     = "#QUOTE#";
 
    /**
     * Hide default constructor
     */
    private JsonUtils() {}
    
    /**
     * Target primitive displayed as JSON.
     *
     * @param value
     *      object value
     * @return
     *      target json expression
     */
    public static final String valueAsJson(Object value) {
        if (value == null ) return "null";
        return "\"" + value.toString() + "\"";
    }
    
    /**
     * Target primitive displayed as JSON.
     *
     * @param value
     *      object value
     * @return
     *      target json expression
     */
    public static final String stringEscapeAsJson(String value) {
        if (value == null ) return "null";
        return "\"" + escapeJson(value) + "\"";
    }
    
    /**
     * Help parsing JSON, escape special char.
     *
     * @param value
     *      current value
     * @return
     *      replacing special characters
     */
    public static final String unEscapeJson(String value) {
        if (value == null ) return null;
        value = value.replaceAll(COMMA,     ",");
        value = value.replaceAll(SEMICOLON, ":");
        value = value.replaceAll(QUOTE,     "\"");
        return value;
    }
    
    public static final String escapeJson(String value) {
        if (value == null ) return null;
        value = value.replaceAll(",",  COMMA);
        value = value.replaceAll(":",  SEMICOLON);
        value = value.replaceAll("\"", QUOTE);
        return value;
    }
    
    public static final String attributeAsJson(String name, Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append(",\"" + name + "\":" + valueAsJson(value));
        return sb.toString();
    }
    
    public static final String objectAsJson(String name, Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append(",\"" + name + "\":" + value);
        return sb.toString();
    }
    
    /**
     * Serialize a collection of object as Json. Element should eventually override <code>toString()</code> to produce JSON.
     *
     * @param pCollec
     *      input collection
     * @return
     *      collection as String
     */
    public static final <T> String collectionAsJson(final Collection < T > pCollec) {
        if (pCollec == null)   return "null";
        if (pCollec.isEmpty()) return "[]";
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        for (T element : pCollec) {
            json.append(first ? "" : ",");
            json.append(valueAsJson(element));
            first = false;
        }
        json.append("]");
        return json.toString();
    }

    /**
     * Serialize a map of objects as Json. Elements should override <code>toString()</code> to produce JSON.
     *
     * { "key1":"value1","key2":"value2" }
     *  
     * @param properties
     *      target properties
     * @return
     *      target json expression
     */
    public static final <K,V> String mapAsJson(final Map<K,V> pMap) {
        if (pMap == null)   return "null";
        if (pMap.isEmpty()) return "{}";
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<K,V> mapEntry : pMap.entrySet()) {
            json.append(first ? "" : ",");
            json.append(valueAsJson(mapEntry.getKey()) + ":");
            json.append(valueAsJson(mapEntry.getValue()));
            first = false;
        }
        json.append("}");
        return json.toString();
    }
    
    /**
     * Convert a Map<String,String> to expected Json escaping characters to parse.
     * The Json value in DB should not be read directly.
     */
    public static final String mapMap2String(Map<String, String > map) {
        return map.keySet()
                  .stream()
                  .filter(k-> null!=k)
                  .map(k -> new StringBuilder(JsonUtils.valueAsJson(k))
                              .append(":")
                              .append(JsonUtils.stringEscapeAsJson(map.get(k)))
                              .toString())
                  .collect(Collectors.joining(",", "{", "}"));
    }
    
    /**
     * Reading a String back to Map<String, String> expecting the special chars to have
     * been escaping with {@link #mapMap2String(Map)}.
     */
    public static final Map<String, String> mapString2Map(String mapAsString) {
       return  Arrays.stream(mapAsString.split(","))
                .map(entry -> entry.split(":"))
                .collect(Collectors.toMap(
                        entry -> entry[0], 
                        entry -> JsonUtils.unEscapeJson(entry[1])));
    }
    
    /**
     * Cache JSON expression for a {@link FF4jRepository}.
     *
     * @param store
     *      current store
     * @return
     *      cache expression
     */
    public static final String cacheJson(FF4jRepository<String, ?> store) {
        StringBuilder sb = new StringBuilder();
        if (store instanceof CacheProxy<?,?>) {
            CacheProxy<?,?> cacheProxy = (CacheProxy<?,?>) store;
            sb.append(",\"cached\":true");
            sb.append(",\"cacheProvider\":\"" + cacheProxy.getCacheProvider() + "\"");
            sb.append(",\"cacheStore\":\"" + cacheProxy.getTargetStore() + "\"");
        } else {
            sb.append(",\"cached\":false");
        }
        return sb.toString();
    }
}
