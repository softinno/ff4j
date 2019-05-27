package org.ff4j.property;

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

import static org.ff4j.test.AssertUtils.assertEquals;
import static org.ff4j.test.AssertUtils.assertNotNull;
import static org.ff4j.test.AssertUtils.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ff4j.property.exception.InvalidPropertyTypeException;
import org.ff4j.user.FF4jUser;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Instanciate properties")
public class PropertyTest {
    
    @Test
    public void mappingWithNullIsOK() {
        assertNull(Property.mapPropertyType(null));
        assertNull(Property.mapSimpleType((String)null));
        assertNull(Property.mapSimpleType((Class<?>)null));
    }
    
    @Test
    public void mappingPropertyTypeShouldForConfiguration() {
        assertEquals("int", Property.mapSimpleType(PropertyInt.class.getName()));
        assertEquals(Map.class.getName(), Property.mapSimpleType(Map.class.getName()));
        assertEquals(Map.class.getName(), Property.mapSimpleType(Map.class));
    }
        
    @Test
    public void parameterizedTypeShouldReturnType() {
        assertEquals(String.class,  new PropertyString("p1").parameterizedType());
    }
    
    @Test
    public void copyConstructorShouldInitProperty() {
        PropertyString p1 = new PropertyString("p1");
        PropertyString p2 = new PropertyString("p2", p1);
        assertNotNull(p2);
        assertEquals("p2", p2.getUid());
        assertEquals(p2.getValueAsString(), p1.getValueAsString());
        p2.setValue(null);
        p2.setFixedValues((Set<String>) null);
        
        Set <String> values = new HashSet<>();
        values.add("v1");values.add("v2");values.add("v3");
        assertThrows(IllegalArgumentException.class,  () -> { 
            p1.addFixedValues(values); 
        });
        
        assertThrows(IllegalArgumentException.class,  () -> { 
            p1.setValue("v4");
        });
        p1.setValue("v1");
        p1.addFixedValues(values);
        p1.addFixedValues((String) null);
        
        PropertyString p3 = new PropertyString("p3", p1);
        assertEquals("p3", p3.getUid());
    }
    
    @Test
    public void propertyCalendat_withCorrectString_isOk() {
        String value = "2018-12-24 23:00:00";
        PropertyCalendar pc = new PropertyCalendar("p1", value);
        assertNotNull(pc);
        assertNotNull(pc.getValue());
        assertEquals(value, pc.getValueAsString());
    }
    
    @Test
    public void propertyCalendar_withIncorrectFormat_shoudlError() {
        assertThrows(IllegalArgumentException.class, () -> { 
            new PropertyCalendar("p1", "20181212-23:00:00"); });
    }
    
    @Test
    public void propertyCalendar_withNull_isDefaultValue() {
        PropertyCalendar pc = new PropertyCalendar("p1", (String) null);
        assertNotNull(pc);
        assertNotNull(pc.getValue());
    }
    
    @Test
    public void propertyByte_withNull_isOK() {
        PropertyByte pb = new PropertyByte("pb", (String) null);
        assertNull(pb.getValue());
        assertThrows(InvalidPropertyTypeException.class, () -> { 
            new PropertyByte("pb", "1238"); });
        assertThrows(InvalidPropertyTypeException.class, () -> { 
            new PropertyByte("pb", ""); });
    }
    
    @Test
    public void propertyBoolean_withInvalidValueThrowErrors() {
        assertThrows(InvalidPropertyTypeException.class, () -> { 
            new PropertyBoolean("pb", "toto"); });
    }
    
    @Test
    public void propertyX_withInvalidValueThrowErrors() {
        assertThrows(InvalidPropertyTypeException.class, () -> { 
            new PropertyDouble("pd", "toto"); });
        assertThrows(InvalidPropertyTypeException.class, () -> { 
            new PropertyInt("pd", "toto"); });
        assertThrows(InvalidPropertyTypeException.class, () -> { 
            new PropertyLong("pd", "toto"); });
        assertThrows(InvalidPropertyTypeException.class, () -> { 
            new PropertyShort("pd", "toto"); });
        assertThrows(InvalidPropertyTypeException.class, () -> { 
            new PropertyBigDecimal("pd", "toto"); });
        assertThrows(InvalidPropertyTypeException.class, () -> { 
            new PropertyBigInteger("pd", "toto"); });
    }
    
    @Test
    public void propertyListInitializationTest() {
        PropertyListString pls1 = new PropertyListString("pls1", "v1", "v2");
        assertNotNull(pls1);
        PropertyListString pls2 = new PropertyListString("pls2", (String[]) null);
        assertNull(pls2.getValue());
        assertNull(pls2.getValueAsString());
        PropertyListString pls3 = new PropertyListString("pls3", "[v1,v2,v3]");
        assertNotNull(pls3);
    }
    
    @SuppressWarnings({"hiding","serial"})
    public static class InvaliPropertyList<FF4jUser> extends PropertyList<FF4jUser, Property<FF4jUser> > {
        public InvaliPropertyList(String uid, String valueAsString) {
            super(uid, valueAsString);
        }
    }
    
    @Test
    public void initPropertyListWithInvalidClassThrowError() {
        assertThrows(IllegalArgumentException.class, () -> { 
            new InvaliPropertyList<FF4jUser>("ipl", null); });
    }
    
    
    @Test
    public void propertySetShouldBeInitLinkeList() {
        PropertySetString pss = new PropertySetString("pss", "v1", "v2");
        assertNotNull(pss.getValue());
    }

}
