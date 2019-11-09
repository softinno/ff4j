package org.ff4j.utils;

/*-
 * #%L
 * ff4j-core
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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MappingTest {
    
    @Test
    @DisplayName("Conversion")
    public void testConvertMap2String() {
        // Given
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("key1", "value1");
        inputMap.put("key2", "val2,val1");
        inputMap.put("key4", "val2\"val1:val3");
        inputMap.put("key5", null);
        // When
        String res = JsonUtils.mapMap2String(inputMap);
        // Then
        Assertions.assertNotNull(res);
        Assertions.assertTrue(res.contains(JsonUtils.COMMA));
        Assertions.assertTrue(res.contains(JsonUtils.QUOTE));
        Assertions.assertTrue(res.contains(JsonUtils.SEMICOLON));
        
        // When
        Map<String, String> outputMap = JsonUtils.mapString2Map(res);
        Assertions.assertEquals(4, outputMap.size());
        
    }

}
