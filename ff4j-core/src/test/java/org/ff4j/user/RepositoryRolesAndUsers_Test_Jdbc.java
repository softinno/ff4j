package org.ff4j.user;

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

import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;

@DisplayName("RepositoryRolesAndUsers::InMemory with JDBC")
@Ignore
public class RepositoryRolesAndUsers_Test_Jdbc {} /*extends RepositoryRolesAndUsersTestSupport {
    
    /** SQL DataSource. *
    private DataSource sqlDataSource;
    
    @Override
    public RolesAndUsersRepository initStore() {
        sqlDataSource = JdbcTestHelper.createInMemoryHQLDataSource();
        return new RolesAndUsersRepositoryJdbc(sqlDataSource);
    }
    
    /** {@inheritDoc} *
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        JdbcTestHelper.initDBSchema(sqlDataSource);
    }
    

}*/
    
