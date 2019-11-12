package org.ff4j.property;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.ff4j.property.Property;
import org.ff4j.property.PropertyFactory;
import org.ff4j.property.PropertyInteger;
import org.junit.jupiter.api.Assertions;

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

import org.junit.jupiter.api.Test;

public class PropertyFactoryTest {
 
    @Test
    public void testInitProperty() {
        Property<?> dynamic = PropertyFactory.createProperty("p1", PropertyInteger.class.getName(), "12");
        Assertions.assertEquals("12", dynamic.getValueAsString());
        Assertions.assertEquals(PropertyInteger.class.getName(), dynamic.getClass().getName());
        assertThrows(IllegalArgumentException.class, 
                () -> { PropertyFactory.createProperty("p1", "Invalid", "12"); });
        
        Property<?> p2 = PropertyFactory.createProperty(dynamic);
        dynamic.add2FixedValueFromString("12");
        dynamic.add2FixedValueFromString("13");
        p2 = PropertyFactory.createProperty(dynamic);
        Assertions.assertEquals("12", p2.getValueAsString());
    }

}
