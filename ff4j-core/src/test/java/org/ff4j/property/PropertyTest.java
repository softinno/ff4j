package org.ff4j.property;

import static org.ff4j.property.Property.mapFromSimple2PropertyType;
import static org.ff4j.property.Property.mapFromProperty2SimpleType;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.ff4j.property.PropertyLogLevel.LogLevel;
import org.ff4j.property.exception.InvalidPropertyTypeException;
import org.ff4j.user.FF4jUser;
import org.ff4j.utils.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Instanciate properties")
public class PropertyTest {
    
    @Test
    public void mappingProperties() {
        // Simple
        assertFalse(mapFromProperty2SimpleType((String)null).isPresent());
        assertFalse(Property.mapFromProperty2SimpleType((Class<?>)null).isPresent());
        assertEquals(Optional.empty(), mapFromProperty2SimpleType("toto"));
        assertEquals(Optional.empty(), mapFromProperty2SimpleType("toto"));
        assertTrue(Property.mapFromProperty2SimpleType(PropertyInteger.class.getName()).isPresent());
        assertEquals("int", mapFromProperty2SimpleType(PropertyInteger.class.getName()).get());
        assertEquals("int", Property.mapFromProperty2SimpleType(PropertyInteger.class).get());
        
        // Property
        assertFalse(mapFromSimple2PropertyType(null).isPresent());
        assertTrue(mapFromSimple2PropertyType("int").isPresent());
        assertEquals(PropertyInteger.class.getName(), mapFromSimple2PropertyType("int").get());
        assertEquals(Optional.empty(), mapFromSimple2PropertyType("toto"));
    }
        
    @Test
    public void parameterizedTypeShouldReturnType() {
        assertEquals(String.class, new PropertyString("p1").parameterizedType());
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
        assertThrows(IllegalArgumentException.class,  () -> { p1.addFixedValues(values); });
        assertThrows(IllegalArgumentException.class,  () -> { p1.setValue("v4"); });
        p1.setValue("v1");
        p1.addFixedValues(values);
        p1.addFixedValues((String) null);
        PropertyString p3 = new PropertyString("p3", p1);
        assertEquals("p3", p3.getUid());
    }
    
    @Test
    public void propertyCalendar() {
        String value = "2018-12-24 23:00:00";
        PropertyCalendar pc = new PropertyCalendar("p1", value);
        assertNotNull(pc);
        assertNotNull(pc.getValue());
        assertEquals(value, pc.getValueAsString());
        assertThrows(IllegalArgumentException.class, () -> { new PropertyCalendar("p1", "20181212-23:00:00"); });
        PropertyCalendar pc2 = new PropertyCalendar("p1", (String) null);
        assertNotNull(pc2);
        assertNull(pc2.getValue());
    }
    
    @Test
    public void propertyClass() {
        PropertyClass pc1 = new PropertyClass("p1", String.class);
        PropertyClass pc2 = new PropertyClass("p2", String.class.getName());
        Assertions.assertEquals(pc1.getValue(), pc2.getValue());
        Assertions.assertEquals(String.class.getName(), pc1.getValueAsString());
        Assertions.assertNull(new PropertyClass("p3", (String) null).getValueAsString());
        Assertions.assertNull(new PropertyClass("p4", (String) null).get());
        assertThrows(IllegalArgumentException.class, 
                () -> { new PropertyClass("pb", "ClassNotExist"); });
    }
    
    @Test
    public void propertyBoolean() {
        PropertyBoolean pBoolean1 = new PropertyBoolean("pBoolean1", Boolean.TRUE);
        PropertyBoolean pBoolean2 = new PropertyBoolean("pBoolean2", "true");
        Assertions.assertEquals(pBoolean1.getValue(), pBoolean2.getValueAsBoolean()); 
        Assertions.assertNull(new PropertyBoolean("pBoolean3", (String) null).get());
        assertThrows(InvalidPropertyTypeException.class, 
                () -> { new PropertyBoolean("pb", "toto"); });
        pBoolean2.setValueFromString("false");
        Assertions.assertFalse(pBoolean2.getValue());
    }
    
    @Test
    public void propertyDate() {
        String value = "2018-12-24 23:00:00";
        PropertyDate pc = new PropertyDate("p1", value);
        assertNotNull(pc);
        assertNotNull(pc.getValue());
        assertEquals(value, pc.getValueAsString());
        assertThrows(IllegalArgumentException.class, () -> { new PropertyDate("p1", "20181212-23:00:00"); });
        PropertyDate pc2 = new PropertyDate("p1", (String) null);
        assertNotNull(pc2);
        assertNull(pc2.getValue());
        assertNull(pc2.getValueAsString());
    }
    
    @Test
    public void propertyInstant() {
        String value = "2018-12-24 23:00:00";
        PropertyInstant pc = new PropertyInstant("p1", value);
        assertNotNull(pc);
        assertNotNull(pc.getValue());
        assertEquals(value, pc.getValueAsString());
        assertThrows(IllegalArgumentException.class, () -> { new PropertyInstant("p1", "20181212-23:00:00"); });
        PropertyInstant pc2 = new PropertyInstant("p1", (String) null);
        assertNotNull(pc2);
        assertNull(pc2.getValue());
        assertNull(pc2.getValueAsString());
    }
    
    @Test
    public void propertyLocateDateTime() {
        String value = "2018-12-24 23:00:00";
        PropertyLocalDateTime pc = new PropertyLocalDateTime("p1", value);
        assertNotNull(pc);
        assertNotNull(pc.getValue());
        assertEquals(value, pc.getValueAsString());
        assertThrows(IllegalArgumentException.class, () -> { new PropertyLocalDateTime("p1", "20181212-23:00:00"); });
        PropertyLocalDateTime pc2 = new PropertyLocalDateTime("p1", (String) null);
        assertNotNull(pc2);
        assertNull(pc2.getValue());
        assertNull(pc2.getValueAsString());
    }
    
    @Test
    public void propertyByte() {
        PropertyByte pb = new PropertyByte("pb", (String) null);
        assertNull(pb.getValue());
        assertThrows(InvalidPropertyTypeException.class, () -> { new PropertyByte("pb", "1238"); });
        assertThrows(InvalidPropertyTypeException.class, () -> { new PropertyByte("pb", ""); });
        
        PropertyByte pByte1 = new PropertyByte("pByte1", "C".getBytes()[0]) ;
        PropertyByte pByte2 = new PropertyByte("pByte2", "C");
        Assertions.assertEquals(pByte1.getValue(), pByte2.getValue());
    }
    
    @Test
    @DisplayName("Testing Property Short initializations")
    public void propertyShort() {
        PropertyShort pShort1 = new PropertyShort("pShort1", (short) 12);
        PropertyShort pShort2 = new PropertyShort("pShort1", "12");
        new PropertyShort("pShort3", Short.valueOf((short)1), Util.setOf((short)1, (short)2, (short)3));
        Assertions.assertEquals(pShort1.getValue(), pShort2.getValue());
        assertThrows(InvalidPropertyTypeException.class, () -> { new PropertyShort("pShort4", "toto"); });
    }
    
    @Test
    @DisplayName("Testing Property Short initializations")
    public void propertyBigDecimal() {
        PropertyBigDecimal pBigDecimal1 = new PropertyBigDecimal("pBigDecimal1", BigDecimal.valueOf(1234));
        PropertyBigDecimal pBigDecimal2 = new PropertyBigDecimal("pBigDecimal2", "1234");
        PropertyBigDecimal pBigDecimal3 = new PropertyBigDecimal("pBigDecimal3", (String) null);
        Assertions.assertEquals(pBigDecimal1.getValue(), pBigDecimal2.getValue());
        Assertions.assertNull(pBigDecimal3.get());
        Assertions.assertNull(pBigDecimal3.getValueAsString());
        Assertions.assertNull(pBigDecimal3.getValueAsString());
        assertThrows(InvalidPropertyTypeException.class, () -> {
            new PropertyBigDecimal("v", "toto");
        });
    }
    
    @Test
    @DisplayName("Testing Property LogLevel")
    public void propertyLogLevel() {
        PropertyLogLevel pl1 = new PropertyLogLevel("log", "DEBUG");
        PropertyLogLevel pl2 = new PropertyLogLevel("log", LogLevel.DEBUG);
        pl1.info().fatal().warn().error().trace().debug();
        Assertions.assertEquals(pl1.getValue(), pl2.getValue());
    }
    
    @Test
    @DisplayName("Testing Property List of String")
    public void propertyListString() {
        PropertyListString pls1 = new PropertyListString("pls1", "v1", "v2");
        assertNotNull(pls1);
        PropertyListString pls2 = new PropertyListString("pls2", (String[]) null);
        assertNull(pls2.getValue());
        assertNull(pls2.getValueAsString());
        PropertyListString pls3 = new PropertyListString("pls3", "[v1,v2,v3]");
        assertNotNull(pls3);
    }
    
    @Test
    @DisplayName("Testing Property List of LogLevel")
    public void propertyListLogLevel() {
        PropertyListLogLevel l1 = new PropertyListLogLevel("l1",  LogLevel.DEBUG,  LogLevel.INFO);
        PropertyListLogLevel l2 = new PropertyListLogLevel("l2", "DEBUG,INFO");
        Assertions.assertEquals(l1.getValue(), l2.getValue());
    }
    
    
    @Test
    @DisplayName("Testing Property List of BigDecimal")
    public void propertyListBigDecimal() {
        PropertyListBigDecimal l1 = new PropertyListBigDecimal("l1", BigDecimal.valueOf(1234), BigDecimal.valueOf(5678));
        PropertyListBigDecimal l2 = new PropertyListBigDecimal("l2", "1234,5678");
        Assertions.assertEquals(l1.getValue(), l2.getValue());
    }
    
    @Test
    @DisplayName("Testing Property List of Bytes")
    public void propertyListByte() {
        PropertyListByte l1 = new PropertyListByte("l1", "C".getBytes()[0], "A".getBytes()[0]);
        PropertyListByte l2 = new PropertyListByte("l2", "C,A");
        Assertions.assertEquals(l1.getValue(), l2.getValue());
    }
    
    @Test
    @DisplayName("Testing Property List of BigInteger")
    public void propertyListBigInteger() {
        PropertyListBigInteger l1 = new PropertyListBigInteger("l1", BigInteger.valueOf(1234), BigInteger.valueOf(5678));
        PropertyListBigInteger l2 = new PropertyListBigInteger("l2", "1234,5678");
        Assertions.assertEquals(l1.getValue(), l2.getValue());
    }
    
    @Test
    @DisplayName("Testing Property List of Short")
    public void propertyListShort() {
        PropertyListShort l1 = new PropertyListShort("l1", Short.valueOf("1"), Short.valueOf("5"));
        PropertyListShort l2 = new PropertyListShort("l2", "1,5");
        Assertions.assertEquals(l1.getValue(), l2.getValue());
    }
    
    @Test
    @DisplayName("Testing Property List of Integer")
    public void propertyListInt() {
        PropertyListInteger l1 = new PropertyListInteger("l1", Integer.valueOf("1"), Integer.valueOf("5"));
        PropertyListInteger l2 = new PropertyListInteger("l2", "1,5");
        Assertions.assertEquals(l1.getValue(), l2.getValue());
        Assertions.assertEquals(l1.getValueAsString(), l2.getValueAsString());
    }

    
    @Test
    @DisplayName("Testing Property List of Classes")
    public void propertyListClass() {
        PropertyListClass l1 = new PropertyListClass("l1", Integer.class, Integer.class);
        PropertyListClass l2 = new PropertyListClass("l2", "java.lang.Integer,java.lang.Integer");
        Assertions.assertEquals(l1.getValue(), l2.getValue());
        Assertions.assertEquals(l1.getValueAsString(), l2.getValueAsString());
        PropertyListClass l3 = new PropertyListClass("pli", (String) null);
        Assertions.assertNull(l3.get());
        Assertions.assertNull(l3.getValueAsString());
    }
    
    @Test
    @DisplayName("Testing Property List of LocalDate")
    public void propertyListLocalDate() {
        String value = "2018-12-24 23:00:00";
        LocalDateTime d1 = LocalDateTime.parse(value, Property.FORMATTER);
        PropertyListLocalDateTime l2 = new PropertyListLocalDateTime("pli", d1,d1);
        PropertyListLocalDateTime l3 = new PropertyListLocalDateTime("pli", "2018-12-24 23:00:00,2018-12-24 23:00:00");
        Assertions.assertEquals(l3.getValue(), l2.getValue());
        Assertions.assertEquals(l3.getValueAsString(), l2.getValueAsString());
        PropertyListLocalDateTime l4 = new PropertyListLocalDateTime("pli", (String) null);
        Assertions.assertNull(l4.get());
        Assertions.assertNull(l4.getValueAsString());
    }
    
    @Test
    @DisplayName("Testing Property List of Long")
    public void propertyListLong() {
        PropertyListLong l1 = new PropertyListLong("l1", Long.valueOf("1"), Long.valueOf("5"));
        PropertyListLong l2 = new PropertyListLong("l2", "1,5");
        Assertions.assertEquals(l1.getValue(), l2.getValue());
    }
    
    @Test
    @DisplayName("Testing Property List of Long")
    public void propertyListFloat() {
        PropertyListFloat l1 = new PropertyListFloat("l1", Float.valueOf("1.5"), Float.valueOf("5.0"));
        PropertyListFloat l2 = new PropertyListFloat("l2", "1.5,5.0");
        Assertions.assertEquals(l1.getValue(), l2.getValue());
    }
    
    @Test
    @DisplayName("Testing Property List of Double")
    public void propertyListDouble() {
        PropertyListDouble l1 = new PropertyListDouble("l1", Double.valueOf("1.5"), Double.valueOf("5.0"));
        PropertyListDouble l2 = new PropertyListDouble("l2", "1.5,5.0");
        Assertions.assertEquals(l1.getValue(), l2.getValue());
    }
    
    @Test
    @DisplayName("Testing Property List of Boolean")
    public void propertyListBoolean() {
        PropertyListBoolean l1 = new PropertyListBoolean("l1", Boolean.TRUE, Boolean.FALSE);
        PropertyListBoolean l2 = new PropertyListBoolean("l2", "true,false");
        Assertions.assertEquals(l1.getValue(), l2.getValue());
    }
    
    @Test
    @DisplayName("Testing Property List of Instant")
    public void propertyListIntant() {
        PropertyListInstant l1 = new PropertyListInstant("pli",
                Instant.ofEpochMilli(1545609600000L),   // new Date(118 , 11, 24, 1, 0, 0).getTime()
                Instant.ofEpochMilli(1577145600000L));  // new Date(119 , 11, 24, 1, 0, 0).getTime()
        String expectedValue = "2018-12-24 00:00:00,2019-12-24 00:00:00";
        PropertyListInstant l2 = new PropertyListInstant("pli", expectedValue);
        PropertyListInstant l3 = new PropertyListInstant("pli", (String) null);
        Assertions.assertNull(l3.get());
        Assertions.assertNull(l3.getValueAsString());
        Assertions.assertEquals(l1.getValue(), l2.getValue());
        Assertions.assertEquals(expectedValue, l1.getValueAsString());
        Assertions.assertEquals(expectedValue, l2.getValueAsString());
    }
    
    @Test
    @DisplayName("Testing Property List of Date")
    public void propertyListDate() {
        PropertyListDate l1 = new PropertyListDate("pli", 
                new Date(1545609600000L-3600000L),
                new Date(1577145600000L-3600000L));
        String expectedValue = "2018-12-24 00:00:00,2019-12-24 00:00:00";
        PropertyListDate l2 = new PropertyListDate("pli", expectedValue);
        PropertyListDate l3 = new PropertyListDate("pli", (String) null);
        Assertions.assertNull(l3.get());
        Assertions.assertNull(l3.getValueAsString());
        Assertions.assertEquals(l1.getValue(), l2.getValue());
        Assertions.assertEquals(expectedValue, l1.getValueAsString());
        Assertions.assertEquals(expectedValue, l2.getValueAsString());
    }
    
    @Test
    @DisplayName("Testing Property List of Calendar")
    public void propertyListCalendar() {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date(1545609600000L-3600000L));
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date(1577145600000L-3600000L));
        PropertyListCalendar l1 = new PropertyListCalendar("pli", c1, c2);
        String expectedValue = "2018-12-24 00:00:00,2019-12-24 00:00:00";
        PropertyListCalendar l2 = new PropertyListCalendar("pli", expectedValue);
        PropertyListCalendar l3 = new PropertyListCalendar("pli", (String) null);
        Assertions.assertNull(l3.get());
        Assertions.assertNull(l3.getValueAsString());
        Assertions.assertEquals(l1.getValue(), l2.getValue());
        Assertions.assertEquals(expectedValue, l1.getValueAsString());
        Assertions.assertEquals(expectedValue, l2.getValueAsString());
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
    @DisplayName("Testing Property Float initializations")
    public void propertyFloat() {
        PropertyFloat pFloat1 = new PropertyFloat("pFloat1", 12.5f);
        PropertyFloat pFloat2 = new PropertyFloat("pFloat2", "12.5");
        Assertions.assertEquals(pFloat1.getValue(), pFloat2.getValue());
        Assertions.assertNull(new PropertyFloat("pFloat3", (String) null).get());
        assertThrows(InvalidPropertyTypeException.class, () -> { new PropertyFloat("pFloat4", "toto"); });
    }
    
    @Test
    @DisplayName("Testing Property Int initializations")
    public void propertyInt() {
        PropertyInteger pInt1 = new PropertyInteger("pInt1", 12);
        PropertyInteger pInt2 = new PropertyInteger("pInt2", "12");
        new PropertyInteger("pInt3", 1, Util.setOf(1,2,3));
        Assertions.assertEquals(pInt1.getValue(), pInt2.getValue());
        Assertions.assertEquals(pInt1.getValue(), pInt2.getValueAsInt());
        assertThrows(InvalidPropertyTypeException.class, () -> { new PropertyInteger("pInt4", "toto"); });
        assertThrows(IllegalArgumentException.class, () -> { new PropertyInteger("pInt4", "1").fromString(null); });
    }
    
    @Test
    @DisplayName("Testing Property Double initializations")
    public void propertyDouble() {
        PropertyDouble pDouble1 = new PropertyDouble("pDouble1", 12.5);
        PropertyDouble pDouble2 = new PropertyDouble("pDouble2", "12.5");
        Assertions.assertEquals(pDouble2.getValue(), pDouble1.getValue());
        Assertions.assertEquals(pDouble2.getValue(), pDouble1.getValueAsDouble());
        Assertions.assertNull(new PropertyDouble("pDouble3", (String) null).get());
        assertThrows(InvalidPropertyTypeException.class, () -> { new PropertyDouble("pDouble4", "toto"); });
    }
    
    @Test
    @DisplayName("Testing Property Long initializations")
    public void propertyLong() {
        // Initialization with Object
        PropertyLong pLong1 = new PropertyLong("pLong1", 12l);
        pLong1.addFixedValue(null);
        Assertions.assertFalse(pLong1.getFixedValues().isPresent());
        // Initialization with String
        PropertyLong pLong2 = new PropertyLong("pLong2", "12");
        // Assessing on values
        Assertions.assertEquals(pLong2.getValue(), pLong1.getValue());
        // Null value is accepted
        Assertions.assertNull(new PropertyLong("pLong3", (String) null).get());
        // Invalid String should throw explicit error
        assertThrows(InvalidPropertyTypeException.class, () -> { new PropertyLong("pLong4", "toto"); });
        
    }
    
    @Test
    public void propertyBigInteger() {
        PropertyBigInteger pBigInteger1 = new PropertyBigInteger("pBigInteger1", BigInteger.valueOf(1234));
        PropertyBigInteger pBigInteger2 = new PropertyBigInteger("pBigInteger2", "1234");
        PropertyBigInteger pBigInteger3 = new PropertyBigInteger("pBigInteger3", (String) null);
        Assertions.assertEquals(pBigInteger1.getValue(), pBigInteger2.getValue());
        Assertions.assertNull(pBigInteger3.get());
        Assertions.assertNull(pBigInteger3.getValueAsString());
        assertThrows(InvalidPropertyTypeException.class, 
                () -> { new PropertyBigInteger("pBigInteger4", "toto"); });
    }
}
