package org.ff4j.property.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.ff4j.core.FF4j;
import org.ff4j.core.config.FF4jConfiguration;
import org.ff4j.core.config.FF4jConfigurationParser;
import org.ff4j.core.exception.AssertionViolationException;
import org.ff4j.core.test.AssertFF4j;
import org.ff4j.core.test.FF4jTestDataSet;
import org.ff4j.core.utils.Util;
import org.ff4j.property.Property;
import org.ff4j.property.PropertyDouble;
import org.ff4j.property.PropertyLogLevel;
import org.ff4j.property.PropertyString;
import org.ff4j.property.PropertyLogLevel.LogLevel;
import org.ff4j.property.exception.PropertyNotFoundException;
import org.ff4j.property.repository.PropertyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*-
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2018 FF4J
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

/**
 * SuperClass to test stores within core project
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
public abstract class AbstractTestSupportPropertyRepository implements FF4jTestDataSet {
    
    /** Initialize */
    protected FF4j ff4j = null;

    /** Tested Store. */
    protected PropertyRepository testedStore;

    /** Test Values */
    protected AssertFF4j assertFF4j;
    
    /** DataSet. **/
    protected FF4jConfiguration testDataSet;
    
    /** {@inheritDoc} */
    @BeforeEach
    public void setUp() throws Exception {
        FF4jConfigurationParser.clearCache();
        testedStore = initStore();
        ff4j        = new FF4j().withRepositoryProperties(testedStore);
        assertFF4j  = new AssertFF4j(ff4j);
        testDataSet = expectConfig();
    }
    
    /**
     * Any store test will declare its store through this callback.
     * 
     * @return working feature store
     * @throws Exception
     *          Hi guys, just let you know I did the update in the presentation : changing instructors names to put the 2 of you    error during building feature store
     */
    protected abstract PropertyRepository initStore();

    // -- exists --
    
    @Test
    public void should_throw_AssertionViolationException_when_invalid_parameters() throws Exception {
        assertThrows(AssertionViolationException.class, () -> { testedStore.exists(null); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.exists(""); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.save((Property<?>)null); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.read(null); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.read(""); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.deleteProperty((String) null); });
    }
    
    @Test
    public void should_create_property_when_save_new() throws Exception {
        assertFF4j.assertThatPropertyStoreHasSize(testDataSet.getProperties().size());
        assertFF4j.assertThatPropertyDoesNotExist(PROPERTY_FOR_TEST);
        PropertyString p = new PropertyString(PROPERTY_FOR_TEST);
        p.setValue("v1").description("ok");
        testedStore.save(p);
        assertFF4j.assertThatPropertyStoreHasSize(testDataSet.getProperties().size() + 1);
        assertFF4j.assertThatPropertyExist(PROPERTY_FOR_TEST);
    }
    
    @Test
    public void should_update_property_when_update() throws Exception {
        assertFF4j.assertThatPropertyDoesNotExist(PROPERTY_FOR_TEST);
        PropertyString p = new PropertyString(PROPERTY_FOR_TEST);
        p.setValue("v1").description("ok");
        testedStore.save(p);
        assertFF4j.assertThatPropertyStoreHasSize(testDataSet.getProperties().size() + 1);
        assertFF4j.assertThatPropertyExist(PROPERTY_FOR_TEST);
    }
    
    @Test
    public void should_create_propertyloglevel_when_save_new() throws Exception {
        assertFF4j.assertThatPropertyStoreHasSize(testDataSet.getProperties().size());
        assertFF4j.assertThatPropertyDoesNotExist(PROPERTY_FOR_TEST);
        PropertyLogLevel p = new PropertyLogLevel(PROPERTY_FOR_TEST, LogLevel.INFO);
        testedStore.save(p);
        assertFF4j.assertThatPropertyStoreHasSize(testDataSet.getProperties().size() + 1);
        assertFF4j.assertThatPropertyExist(PROPERTY_FOR_TEST);
    }  
    
    @Test
    public void should_match_dataset_when_read_all() {
        // Given
        assertFF4j.assertThatPropertyStoreHasSize(testDataSet.getProperties().size());
        // When
        Map < String, Property<?> > properties = Util.toMap(testedStore.findAll());
        // Then
        Assertions.assertEquals(testDataSet.getProperties().size(), properties.size());
    } 
    
    // -- update --
    
    @Test
    public void should_create_new_property_when_save_new() throws Exception {
        assertFF4j.assertThatPropertyDoesNotExist(PROPERTY_FOR_TEST);
        testedStore.save(new PropertyString(PROPERTY_FOR_TEST, "OK"));
        assertFF4j.assertThatPropertyExist(PROPERTY_FOR_TEST);
    }
    
    @Test
    public void should_update_property_when_save() {
        // Givens
        String newDescription = "new-description";
        assertFF4j.assertThatPropertyExist(PDouble);
        Assertions.assertFalse(testedStore.read(PDouble).getDescription().isPresent());
        Assertions.assertFalse(testedStore.read(PDouble).getFixedValues().isPresent());
        // When
        PropertyDouble propDouble = (PropertyDouble) testedStore.read(PDouble);
        propDouble.setDescription(newDescription);
        propDouble.add2FixedValueFromString("20.0");
        propDouble.add2FixedValueFromString("10.0");
        propDouble.add2FixedValueFromString("30.0");
        testedStore.save(propDouble);
        // Then
        PropertyDouble updatedProp = (PropertyDouble) testedStore.read(PDouble);
        Assertions.assertTrue(newDescription.equals(updatedProp.getDescription().get()));
        Assertions.assertTrue(updatedProp.getFixedValues().isPresent());
        Assertions.assertEquals(3, testedStore.read(PDouble).getFixedValues().get().size());
    }
    
    @Test
    public void should_return_empty_if_property_not_exist() {
        // Givens
        assertFF4j.assertThatPropertyDoesNotExist("PPP");
        assertFF4j.assertThatPropertyExist(PDouble);
        // When
        Optional<Property<?>> p1 = testedStore.find("PPP");
        Optional<Property<?>> p2 = testedStore.find(PDouble);
        // Then
        Assertions.assertTrue(p1.isEmpty());
        Assertions.assertFalse(p2.isEmpty());
    }
    
    @Test
    public void should_update_property_when_update_value() {
        // Givens
        assertFF4j.assertThatPropertyExist(PDouble);
        // When
        PropertyDouble propDouble = (PropertyDouble) testedStore.read(PDouble);
        Assertions.assertNotEquals(10.1, propDouble.getValue());
        testedStore.updatePropertyValue(PDouble, String.valueOf(10.1));
        // Then
        PropertyDouble updatedProp = (PropertyDouble) testedStore.read(PDouble);
        Assertions.assertEquals(10.1, updatedProp.getValue());
    }
    
    // -- delete --
    
    @Test
    public void should_remove_property_when_delete() throws Exception {
        assertFF4j.assertThatPropertyStoreHasSize(testDataSet.getProperties().size());
        assertFF4j.assertThatPropertyExist(PDouble);
        testedStore.delete(PDouble);
        assertFF4j.assertThatPropertyStoreHasSize(testDataSet.getProperties().size() - 1 );
        assertFF4j.assertThatPropertyDoesNotExist(PDouble);
    }
    
    @Test
    public void should_throw_PropertyNotFoundException_when_delete_notexisting_property() throws Exception {
        assertFF4j.assertThatPropertyDoesNotExist(PROPERTY_FOR_TEST);
        assertThrows(PropertyNotFoundException.class, () -> { 
            testedStore.delete(PROPERTY_FOR_TEST); 
        });
    }
    
    @Test
    public void should_empty_repository_when_deleteAll() {
        // Given
        assertFF4j.assertThatPropertyExist(PDouble);
        // When
        testedStore.deleteAll();
        // Then
        Assertions.assertEquals(0, testedStore.findAll().count());
    }
    
    @Test
    public void should_work_when_creatingSchema() {
        testedStore.createSchema();
    }
    
    @Test
    public void should_match_expecteddataset_when_findAllIds() {
        Stream< String> propertyNames = testedStore.findAllIds();
        Assertions.assertNotNull(propertyNames);
        Assertions.assertEquals(testDataSet.getProperties().size(), propertyNames.count());
    }
        
}
