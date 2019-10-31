package org.ff4j.features;

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

import org.apache.commons.dbcp.BasicDataSource;
import org.ff4j.feature.repository.FeatureRepository;
import org.ff4j.feature.repository.FeatureRepositoryJdbc;
import org.junit.jupiter.api.Test;

public class RepositoryFeatureJdbcCreateTableTest {
    
    /** {@inheritDoc} */
    @Test
    public void testCreateSchema() {
        BasicDataSource dbcpDataSource = new BasicDataSource();
        dbcpDataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dbcpDataSource.setUsername("sa");
        dbcpDataSource.setPassword("");
        dbcpDataSource.setUrl("jdbc:hsqldb:mem:ok");
        dbcpDataSource.setMaxActive(3);
        dbcpDataSource.setMaxIdle(2);
        dbcpDataSource.setInitialSize(2);
        dbcpDataSource.setValidationQuery("select 1 from INFORMATION_SCHEMA.SYSTEM_USERS;");
        dbcpDataSource.setPoolPreparedStatements(true);
        FeatureRepository repo = new FeatureRepositoryJdbc(dbcpDataSource);
        repo.createSchema();
    }
    
}
