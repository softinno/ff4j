package org.ff4j.cassandra.store;

/*-
 * #%L
 * ff4j-store-cassandra
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

import java.util.Optional;
import java.util.stream.Stream;

import org.ff4j.feature.Feature;
import org.ff4j.feature.repository.FeatureRepository;
import org.ff4j.feature.repository.FeatureRepositorySupport;

/**
 * Implementation of {@link FeatureRepository} to store data into Apache Cassandra™.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
public class FeatureRepositoryCassandra  extends FeatureRepositorySupport {

    /** Serial. */
    private static final long serialVersionUID = 1230721832994192438L;

    /** {@inheritDoc} */
    @Override
    public boolean existGroup(String groupName) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public Stream<Feature> readGroup(String groupName) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Stream<String> listGroupNames() {
        return null;
    }

    @Override
    public void saveFeature(Feature feature) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteFeature(String uid) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean exists(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Stream<String> findAllIds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Feature> find(String id) {
        // TODO Auto-generated method stub
        return null;
    }

}
