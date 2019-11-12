package org.ff4j.feature.repository;

import java.util.Collection;

import org.ff4j.core.test.TestConfigurationParser;

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
import org.ff4j.feature.repository.FeatureRepositoryInMemory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testing implementation of {@link FeatureRepository} for DB : MEMORY
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@DisplayName("RepositoryFeature_InMemory_TestFixedDataSet")
public class FeatureRepositoryTest_InMemory extends AbstractTestSupportFeatureRepository {

    /** {@inheritDoc} */
    @Override
    public FeatureRepository initStore() {
        return new FeatureRepositoryInMemory(
                new TestConfigurationParser(), "ff4j-testDataset.xml");
    }
    
    @Test
    @SuppressWarnings({"unchecked","rawtypes"})
    public void should_init_with_file() {
        Assertions.assertNotNull(new FeatureRepositoryInMemory(
                new TestConfigurationParser(), "ff4j-testDataset.xml"));
        Assertions.assertNotNull(new FeatureRepositoryInMemory(
                new TestConfigurationParser(), 
                    getClass().getClassLoader().getResourceAsStream("ff4j-testDataset.xml")));
        Assertions.assertNotNull(new FeatureRepositoryInMemory( (Collection) null));
    }
    
}
