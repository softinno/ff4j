package org.ff4j.event.repository.audit;

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

import javax.sql.DataSource;

import org.ff4j.core.jdbc.JdbcSchema.AuditTrailColumns;
import org.ff4j.core.jdbc.JdbcUtils;
import org.ff4j.event.repository.audit.AuditTrailRepository;
import org.ff4j.event.repository.audit.AuditTrailRepositoryJdbc;
import org.ff4j.test.jdbc.JdbcTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

/**
 * Testing live audit.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@DisplayName("RepositoryAudit_Jdbc_Test")
public class AuditTrailJdbcTest extends AbstractTestSupportAuditTrail {

    /** SQL DataSource. */
    private DataSource sqlDataSource;
    
    /** {@inheritDoc} */
    protected AuditTrailRepository initAuditTrailRepository() {
        sqlDataSource = JdbcTestHelper.createInMemoryHQLDataSource();
        return new AuditTrailRepositoryJdbc(sqlDataSource);
    }
    
    /** {@inheritDoc} */
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        JdbcTestHelper.initDBSchema(sqlDataSource);
        JdbcUtils.isTableExist(sqlDataSource, AuditTrailColumns.NAME.tableName());
    }

}
