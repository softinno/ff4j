package org.ff4j.property;

import java.util.Collection;

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

import org.ff4j.feature.repository.FeatureRepository;
import org.ff4j.property.repository.PropertyRepository;
import org.ff4j.property.repository.PropertyRepositoryInMemory;
import org.ff4j.test.TestConfigurationParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testing implementation of {@link FeatureRepository} for DB : MEMORY
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@DisplayName("PropertyRepository::InMemory with static DataSet")
public class RepositoryPropertyInMemoryTest extends PropertyRepositoryTestSupport {

    /** {@inheritDoc} */
    @Override
    public PropertyRepository initStore() {
        return new PropertyRepositoryInMemory(new TestConfigurationParser(), "ff4j-testDataset.xml");
    }
    
    @Test
    @SuppressWarnings({"unchecked","rawtypes"})
    public void initStoreWithFile() {
        Assertions.assertNotNull(new PropertyRepositoryInMemory(
                new TestConfigurationParser(), "ff4j-testDataset.xml"));
        Assertions.assertNotNull(new PropertyRepositoryInMemory(
                new TestConfigurationParser(), 
                    getClass().getClassLoader().getResourceAsStream("ff4j-testDataset.xml")));
        Assertions.assertNotNull(new PropertyRepositoryInMemory( (Collection) null));
    }
    
    @Test
    public void should_return_null_when_properties_null() {
        // Given
        PropertyRepositoryInMemory pStoreMem = new PropertyRepositoryInMemory();
        // When
        pStoreMem.setProperties(null);
        // Then
        Assertions.assertNull(pStoreMem.findAll());
        Assertions.assertNull(pStoreMem.findAllIds());
    }
    
}
